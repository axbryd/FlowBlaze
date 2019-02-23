import registers as reg
import os
import parser
from fb_defines import *
import serial
import numpy as np

from utils import isAnUpdate, hton
import subprocess

WRITE_END_STR = ", f, -."
READ_START_STR = "-, -, -,"
SEPARATOR = ", "
STR_FORMAT_32 = "{0:0{1}x}"
STR_FORMAT_128 = "{0:0{1}x}"

SERIAL_MODE = 0
RWAXI_MODE = 1
SIM_MODE = 2
FILE_MODE = 3
USB_MODE = 4
BASH_MODE = 5

NUM_LANES = 4
NUM_STAGES = 3

RWAXI_PATH = "../rwaxi "

SIM = 0

header_fields = {"XTRA_SRC_MAC": {'len': 6, 'offset': 6},
                 "XTRA_DST_MAC": {'len': 6, 'offset': 0},
                 "XTRA_ETH_TYPE": {'len': 2, 'offset': 12},
                 "XTRA_VLAN_ID": {'len': 3, 'offset': 15},
                 "XTRA_IPv4_SRC": {'len': 4, 'offset': 26},
                 "XTRA_IPv4_DST": {'len': 4, 'offset': 30},
                 "XTRA_IPv4_PROTO": {'len': 1, 'offset': 23},
                 "XTRA_TCP_SPORT": {'len': 2, 'offset': 34},
                 "XTRA_TCP_DPORT": {'len': 2, 'offset': 36},
                 "XTRA_UDP_SPORT": {'len': 2, 'offset': 34},
                 "XTRA_UDP_DPORT": {'len': 2, 'offset': 36}}


class Programmer:
    def __init__(self, mode=0, out_file_path=None, json_file="out.json", rwaxi_path=RWAXI_PATH, serial_name=None):
        self.mode = mode
        self.rwaxi_path = rwaxi_path
        self.parsed = parser.Parser(json_file)
        self.hfs = self.parsed.hfs
        if mode == SERIAL_MODE:
            if serial_name:
                self.ser = serial.Serial(serial_name, 115200, timeout=0.02)
        elif mode == FILE_MODE:
            self.out_file = open(out_file_path, 'w')
            self.out_file.write("// Flow blaze application\n\n")
            self.out_file.write("void compiled_app() {\n")
        elif mode == SIM_MODE:
            self.out_file = open(out_file_path, 'w')
        elif mode == USB_MODE:
            self.out_file = out_file_path
        elif mode == BASH_MODE:
            self.out_file = open(out_file_path, 'w')
            self.out_file.write("#!/bin/bash\n")

    def finish(self):
        if self.mode == FILE_MODE:
            self.out_file.write("}\n")
        if self.mode != USB_MODE:
            self.out_file.close()

    def write_register(self, addr, value):
        if self.mode == SIM_MODE:
            print("# Writing register")

            buf = STR_FORMAT_32.format(addr, 8) + SEPARATOR + \
                  STR_FORMAT_32.format(value, 8) + WRITE_END_STR + "\n"
            self.out_file.write(buf)
            print(buf)

        elif self.mode == RWAXI_MODE:
            os.system(self.rwaxi_path + " -a 0x" + STR_FORMAT_32.format(addr, 8) +
                      " -w 0x" + STR_FORMAT_32.format(value, 8))

        elif self.mode == SERIAL_MODE:
            self.ser.write(b'W 0x' + STR_FORMAT_32.format(addr, 8) + " 0x" + STR_FORMAT_32.format(value, 8) + " \r")
            print("writing: "+str(b'W 0x') + STR_FORMAT_32.format(addr, 8) + " 0x" + STR_FORMAT_32.format(value, 8) + " \\r")

            line = "0"
            self.ser.write(b'R 0x' + STR_FORMAT_32.format(addr, 8) + " 0x00 \r")
            while line:
                line = self.ser.readline()
                print(line)

        elif self.mode == FILE_MODE:
            string = "\tregwrite(0x" + STR_FORMAT_32.format(addr, 8) + ", 0x" + STR_FORMAT_32.format(value, 8) + ");\n"
            self.out_file.write(string)
            print("0x" + STR_FORMAT_32.format(addr, 8) + ", 0x" + STR_FORMAT_32.format(value, 8))

        elif self.mode == USB_MODE:
            string = "echo -ne '97 0x" + STR_FORMAT_32.format(addr, 8) + \
                     " 0x" + STR_FORMAT_32.format(value,8) + "\\r' > " + self.out_file + "; sleep 0.03;\n"
            subprocess.call(["bash", "-c", string])

            print(string)
        elif self.mode == BASH_MODE:
            string = "echo -ne \"97 0x" + STR_FORMAT_32.format(addr, 8) + \
                     " 0x" + STR_FORMAT_32.format(value, 8) + "\\r\" > /dev/ttyUSB1; sleep 0.03;\n"
            self.out_file.write(string)
            print(string)

        else:
            print("Mode not supported")
            exit(-1)

    # normally tcam1 has only dontcares except if we want to differentiate
    # default states for example with respect to ports
    def write_tcam1_entry(self, entry_num=0):
        self.write_register(reg.RAM1[0], 0)
        self.write_register(reg.TCAM1_DATA_IN, 0)
        for i in range(len(reg.TCAM1_MASK)):
            address = reg.TCAM1_MASK[i] | entry_num * 4 << 6
            self.write_register(address, 0xffffffff)

    def write_tcam2(self, stage):
        conditions = self.parsed.conditions[stage]

        for i, e in enumerate(self.parsed.entries[stage]):
            self.write_tcam2_entry(i, e['state'], e['results'], conditions, stage)

    def write_tcam2_entry(self, entry_num, state, results, conditions, stage):
        value = [0, 0, 0, 0]
        mask = [0xffffffff for i in range(8)]

        j = 0
        for i, r in enumerate(results):
            cond = conditions[i]
            if r == FB_DONTCARE:
                if not cond['exact_match']:
                    j += 1
                continue

            if cond["exact_match"]:
                # handle exact match fields
                # I have to take the current condition and take the second operand
                # based on the field, selected with the right mask
                pos = find_hf_position(self.parsed.hfs[0], cond['op0'])

                m = mask_from_field(cond['op0'])

                if cond['op0']['type'] == "XTRA_EVENT_FIELD":
                    value[pos] |= (reg.PORTS[int(cond['op1'])-1] & 0xff) << 24
                    value[pos + 1] |= (reg.PORTS[int(cond['op1'])-1] >> 8)
                else:
                    value[pos] |= (hton(int(cond['op1']), m) & 0xff) << 24
                    value[pos+1] |= (hton(int(cond['op1']), m) >> 8)

                mask[pos] &= (0x00ffffff | (m << 24))
                mask[pos+1] &= (0xff000000 | (m >> 8))
                continue
            elif r == FB_TRUE and not cond['exact_match']:
                value[0] |= 1 << j + 8
                mask[0] &= ~(1 << j + 8)
            elif r == FB_FALSE and not cond['exact_match']:
                mask[0] &= ~(1 << j + 8)
            else:
                print("Error: unrecognized condition")
                exit(-1)

            j += 1

        if state != 255:
            value[0] |= state & 0xff
            mask[0] &= 0xffffff00

        print(RED + "\n########## Write tcam2 entry #" + str(entry_num) + " ###########" + RESET)
        for i in range(len(reg.TCAM2_DATA_IN)):
            self.write_register(reg.TCAM2_DATA_IN[i], value[i])

        print("############ WRITING TCAM2 MASK  ############")
        for i in range(len(reg.TCAM2_DEFAULT_MASK)):
            address = reg.TCAM2_MASK[i] | (entry_num << 6)
            self.write_register(address, mask[i])

    def write_actions(self, entry_num, actions, next_state, curr_state):
        ram2_value = next_state & 0xff
        ram2_address = reg.RAM2[entry_num]

        ram3_value = reg.HASH_INSERT
        ram3_address = reg.RAM3[entry_num]
        ram4_value = 0
        ram4_address = reg.RAM4[entry_num]

        pipealu_words = self.pipe_alu_actions_allocator(actions, curr_state)

        for a in actions:
            if isAnUpdate(a['opcode']):
                continue
            elif a['opcode'] == 'XTRA_SENDPKT':
                port = int(a['op1']['value'])

                if port == 255:
                    ram3_value |= reg.PORT_FLOOD << 24
                    continue

                if port not in range(4):
                    print("ERROR: port number not recognized")
                    break

                if port != 0:
                    ram3_value |= reg.PORTS[port-1] << 24

            elif a['opcode'] == 'XTRA_SETFIELD':
                ram3_value |= reg.MODIFY_FIELD

                op0 = a['op0']
                op2 = a['op2']

                if op0['type'] != 'XTRA_PKT_FIELD':
                    print("ERROR: set field action not valid")
                    break
                if op2['type'] != 'XTRA_INTEGER_CONST':
                    print("ERROR: set field action not valid")
                    break

                offset = int(op0['from'])
                length = int(op0['length'])
                value = hton(int(op2['value']))

                ram4_value |= offset | (length << 14) | (value << 16)

            elif a['opcode'] == 'XTRA_PUSHFIELD':
                ram3_value |= reg.PUSH_FIELD

                op0 = a['op0']
                op2 = a['op2']

                if op0['type'] != 'XTRA_PKT_FIELD':
                    print("ERROR: set field action not valid")
                    break
                if op2['type'] != 'XTRA_INTEGER_CONST':
                    print("ERROR: set field action not valid")
                    break

                offset = int(op0['from'])
                length = int(op0['length'])
                value = int(op2['value'])

                ram4_value |= offset | ((length << 14) & 0x3) | (value << 16)
            else:
                print("ERROR: action not recognized/implemented")

        ram2_value |= (entry_num << 8)

        print("Writing ram2: next state [0-7], instruction for pipe alu [8-15]")
        self.write_register(ram2_address, ram2_value)
        print("Writing ram3: "
              "\t- 0-15  -> table_insert/delete\n"
              "\t- 16-23 -> push/modify field\n"
              "\t- 24-31 -> select dst interface")
        self.write_register(ram3_address, ram3_value)
        print("Writing ram4: -> action values for push/modify fields\n"
                       "\t- 0-13  -> offset\n"
                       "\t- 14-15 size (0 is 8bit and so on...)\n"
                       "\t- 16-31 value")
        self.write_register(ram4_address, ram4_value)

        print("Writing pipealu")
        for i, word in enumerate(pipealu_words):
            self.write_register(reg.PIPEALU[entry_num]+(i*4), word)

    def write_conditions(self, conditions):
        i = 0
        for c in conditions:
            if not c["exact_match"] and i < 4:
                self.write_condition(c, i)
                i += 1
            elif not c["exact_match"] and i >= 4:
                print("error: overflow of conditions")
                exit(-1)
            else:
                continue

    def write_condition(self, condition, id):
        addr = reg.COND_REG[id]

        op0 = condition['op0'] & 0xff
        op1 = condition['op1'] & 0xff
        opcode = 0

        if condition['opcode'] == "XTRA_GREATER":
            opcode = reg.FB_GREATER
        elif condition['opcode'] == "XTRA_GREATER_EQ":
            opcode = reg.FB_GREATER_EQ
        elif condition['opcode'] == "XTRA_LESS":
            opcode = reg.FB_LESS
        elif condition['opcode'] == "XTRA_LESS_EQ":
            opcode = reg.FB_LESS_EQ
        elif condition['opcode'] == "XTRA_EQUAL":
            opcode = reg.FB_EQ
        elif condition['opcode'] == "XTRA_NOT_EQUAL":
            opcode = reg.FB_NOT_EQ
        else:
            print("Error: opcode not recognized")

        value = (opcode << 16) | (op0 << 8) | op1

        self.write_register(addr, value)

    def write_flow_key(self, fk):
        value = fk["off1"] | fk["off2"] << 16
        mask = [(fk["mask"] >> i*32) & 0xffffffff for i in range(0, 4)]

        self.write_register(reg.LOOKUP_SCOPE, value)

        for i in range(0, 4):
            self.write_register(reg.MASK_LOOKUP[i], mask[i])

    def write_update_key(self, ufk):
        value = ufk['off1'] | ufk['off2'] << 16
        mask = [(ufk["mask"] >> i * 32) & 0xffffffff for i in range(0, 4)]

        self.write_register(reg.UPDATE_SCOPE, value)

        for i in range(0, 4):
            self.write_register(reg.MASK_UPDATE[i], mask[i])

    def write_header_field(self, offset, length, nb_hf):
        # nb_hf starts from 0
        if length < 1 or length > 4:
            print("error length not supported")
        if nb_hf < 0 or nb_hf > 3:
            print("error: maximum number of HFs is 3")

        value = (offset & 0x3f) | ((length-1) << 6)

        self.write_register(reg.HFS[nb_hf], value)

    def write_header_fields(self, hfs):
        # for s in stages:

        for i, hf in enumerate(hfs):
            if hf['type'] == 'XTRA_EVENT_FIELD':
                length = 1
                offset = 38
            else:
                length = int(hf['length'])
                offset = int(hf['offset'])

            self.write_header_field(offset, length, i)

    def pipe_alu_actions_allocator(self, actions, state):
        packed_inputs = []
        words = [0 for i in range(9)]

        nb_actions = 0
        for a in actions:
            if not isAnUpdate(a["opcode"]):
                continue

            if a['op2']['type'] == "XTRA_GLOBAL_REGISTER" and state == 0 and\
                a['op0']['type'] == "XTRA_INTEGER_CONST":
                print("Configuring global register")
                self.write_register(reg.GRS[int(a['op2']['name'])], int(a['op0']['value']))
                continue

            nb_actions += 1

            input0 = [a["op0"]["type"],
                      a["op0"]["value"] if a["op0"]["type"] == "XTRA_INTEGER_CONST" else a["op0"]["name"]]
            input1 = [a["op1"]["type"],
                      a["op1"]["value"] if a["op1"]["type"] == "XTRA_INTEGER_CONST" else a["op1"]["name"]]

            if input0 not in packed_inputs:
                packed_inputs.append(input0)

            if input1 not in packed_inputs:
                packed_inputs.append(input1)

        if nb_actions > 4 or len(packed_inputs) > 4:
            print("Error: too much actions")
            exit(-1)

        for i, pin in enumerate(packed_inputs):
            w = 0
            if pin[0] == "XTRA_LOCAL_REGISTER":
                w = reg.REGS_OFFSET[int(pin[1])]
            elif pin[0] == "XTRA_GLOBAL_REGISTER":
                w = reg.GRS_OFFSET[int(pin[1])]
            elif pin[0] == "XTRA_INTEGER_CONST":
                w = ((int(pin[1]) & 0xff) << 16 | (int(pin[1]) & 0xff00) << 8) | 0xd5000000
            elif pin[0] == "XTRA_READ_ONLY_REGISTER":
                w = reg.TS_OFFSET
            else:
                print("ERROR: don't know the type")

            words[i] = w

        words[8] = 0x00000000

        for a in actions:
            opc = 0

            if not isAnUpdate(a['opcode']) \
                    or a['op2']['type'] == "XTRA_GLOBAL_REGISTER" \
                    and state == 0 and \
                    a['op0']['type'] == "XTRA_INTEGER_CONST":
                continue

            if a['op2']['type'] == "XTRA_GLOBAL_REGISTER":
                words[8] = 0x00000080

            if a['opcode'] == "XTRA_SUM":
                opc = reg.FB_PLUS
            elif a['opcode'] == "XTRA_MINUS":
                opc = reg.FB_MINUS
            else:
                print("Error: opcode not implemented")

            input0 = [a["op0"]["type"],
                      a["op0"]["value"] if a["op0"]["type"] == "XTRA_INTEGER_CONST" else a["op0"]["name"]]
            input1 = [a["op1"]["type"],
                      a["op1"]["value"] if a["op1"]["type"] == "XTRA_INTEGER_CONST" else a["op1"]["name"]]

            op0 = 0
            op1 = 0
            for i, p in enumerate(packed_inputs):
                if input0 == p:
                    op0 = i

                if input1 == p:
                    op1 = i

            pos = int(a['op2']['name'])

            w = op0 | (op1 << 3) | (opc << 8)
            if pos == 0:
                words[4] |= w
            elif pos == 1:
                words[4] |= (w << 16)
            elif pos == 2:
                words[5] |= w
            elif pos == 3:
                words[5] |= (w << 16)

        words[6] = 0x00010000
        words[7] = 0x00030002

        if nb_actions == 0:
            words[0] = 0x36
            words[1] = 0x3a
            words[2] = 0x3e
            words[3] = 0x0
            words[4] = 0x00010000
            words[5] = 0x00030002
            words[6] = 0x00010000
            words[7] = 0x00030002
            words[8] = 0x00000000

        return words
