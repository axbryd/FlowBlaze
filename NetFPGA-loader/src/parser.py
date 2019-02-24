import json
from fb_defines import *
import registers

stages = "stages"
table = "table"
table_rows = "table_rows"
stage_label = "stage_label"
flow_key = "flow_key"
update_key = 'update_key'
fk_offset = "from"
fk_len = "length"
condition_label = "conditions"
condition_results = "condition_results"


class Parser:
    def __init__(self, filename=None):
        if filename is not None:
            with open(filename) as f:
                self.program = json.load(f)
        self.stages = []
        self.flow_keys = []
        self.update_keys = []
        self.conditions = []  # conditions = [ [{op1, op2, opcode, exact}, {...}], [{...}, ..., {...}] ]
        self.results = []  # [ [FB_TRUE, FB_FALSE, EXACT ...], ..., [...] ]
        self.hfs = []  # hfs = [ [HF1_NAME, HF2_NAME, HF3_NAME], ..., [...] ]
        self.entries = []

    def pack_entries(self):
        stages = self.program['stages']

        for s in stages:
            self.entries.append([])

            for e in s['table']['table_rows']:
                results = self.get_conditions_results(e, int(s['stage_label']))

                entry = {'state': int(e['state']), 'next_state': int(e['next_state']),
                         'results': results, 'actions': e['actions']}

                self.entries[int(s['stage_label'])].append(entry)

    def pack_flow_keys(self):
        fks = []
        for stage in self.program[stages]:
            last_len = 0
            off1_full, off2_full, off2_start = False, False, False
            off1, off2 = 0, 0
            mask1, mask2 = 0, 0
            i = 0

            for field in stage[flow_key]:
                if i == 0:
                    off1 = int(field[fk_offset])

                    if int(field[fk_len]) > 8:
                        print("ERROR: first field is too long")
                    else:
                        for i in range(0, int(field[fk_len])):
                            mask1 |= 0xff << i * 8
                        last_len = int(field[fk_len])
                else:
                    if off1_full is False:
                        # if fields are contiguous
                        if int(field[fk_offset]) == off1 + last_len and int(field[fk_len]) <= 8 - last_len:
                            for i in range(last_len, last_len + int(field[fk_len])):
                                mask1 |= 0xff << i * 8
                            off1_full = True
                        else:
                            print("ERROR: second field is too long")
                            exit(-1)
                    else:
                        if off2_start is False:
                            off2_start = True
                            off2 = int(field[fk_offset])

                            if int(field[fk_len]) > 8:
                                print("Error: second field too long")
                                exit(-1)
                            else:
                                for i in range(0, int(field[fk_len])):
                                    mask2 |= 0xff << i * 8
                        else:
                            if off2_full is False:
                                # check if fields are contiguous
                                if int(field[fk_offset]) == off1 + last_len and int(field[fk_len]) <= 8 - last_len:
                                    for i in range(last_len, last_len + int(field[fk_len])):
                                        mask2 |= 0xff << i * 8
                                    off2_full = True
                                else:
                                    print("ERROR: second field is too long")
                                    exit(-1)

                i += 1

                mask = mask1 | (mask2 << 64)

                fks.append({"off1": off1, "off2": off2, "mask": mask})

            self.flow_keys = fks

    def pack_update_keys(self):
        ufks = []

        for stage in self.program[stages]:
            last_len = 0
            off1_full, off2_full, off2_start = False, False, False
            off1, off2 = 0, 0
            mask1, mask2 = 0, 0
            i = 0

            for field in stage[update_key]:
                if i == 0:
                    off1 = int(field[fk_offset])

                    if int(field[fk_len]) > 8:
                        print("ERROR: first field is too long")
                    else:
                        for i in range(0, int(field[fk_len])):
                            mask1 |= 0xff << i * 8
                        last_len = int(field[fk_len])
                else:
                    if off1_full is False:
                        # if fields are contiguous
                        if int(field[fk_offset]) == off1 + last_len and int(field[fk_len]) <= 8 - last_len:
                            for i in range(last_len, last_len + int(field[fk_len])):
                                mask1 |= 0xff << i * 8
                            off1_full = True
                        else:
                            print("ERROR: second field is too long")
                            exit(-1)
                    else:
                        if off2_start is False:
                            off2_start = True
                            off2 = int(field[fk_offset])

                            if int(field[fk_len]) > 8:
                                print("Error: second field too long")
                                exit(-1)
                            else:
                                for i in range(0, int(field[fk_len])):
                                    mask2 |= 0xff << i * 8
                        else:
                            if off2_full is False:
                                # check if fields are contiguous
                                if int(field[fk_offset]) == off1 + last_len and int(field[fk_len]) <= 8 - last_len:
                                    for i in range(last_len, last_len + int(field[fk_len])):
                                        mask2 |= 0xff << i * 8
                                    off2_full = True
                                else:
                                    print("ERROR: second field is too long")
                                    exit(-1)
                i += 1

                mask = mask1 | (mask2 << 64)

                ufks.append({"off1": off1, "off2": off2, "mask": mask})

            self.update_keys = ufks

    def pack_conditions(self):
        # iterate stages
        for s in self.program[stages]:
            conds = s[table][condition_label]

            packed_conditions = []
            self.hfs.append([])

            # iterate conditions
            for c in conds:
                packed_c = {"exact_match": False}
                # case with exact match on header field
                # this goes directly on tcam2 
                if (c['op0']['type'] == "XTRA_PKT_FIELD" or c['op0']['type'] == "XTRA_EVENT_FIELD") \
                        and c['op1']['type'] == "XTRA_INTEGER_CONST" \
                        and c['opcode'] == "XTRA_EQUAL":
                    packed_c["exact_match"] = True
                    if c['op0']['type'] == "XTRA_PKT_FIELD":
                        packed_c['op0'] = {'type': "XTRA_PKT_FIELD", 'offset': c['op0']['from'],
                                           'length': c['op0']['length']}
                    else:
                        packed_c['op0'] = c['op0']
                    packed_c['op1'] = c['op1']['value']
                    packed_c['opcode'] = c['opcode']

                    # append the header field to the header fields list
                    if packed_c['op0'] not in self.hfs[int(s['stage_label'])]:
                        self.hfs[int(s['stage_label'])].append(packed_c['op0'])

                elif c['op0']['type'] == "XTRA_EVENT_FIELD":
                    packed_c["exact_match"] = True
                    packed_c['op0'] = c['op0']['value']
                    packed_c['op1'] = c['op1']['value']
                    packed_c['opcode'] = c['opcode']
                # case with GR/R/HF (not exact match)
                else:
                    packed_c['exact_match'] = False
                    if c['op0']['type'] == "XTRA_LOCAL_REGISTER":
                        packed_c['op0'] = registers.COND_LRS[int(c['op0']['name'])]
                    elif c['op0']['type'] == "XTRA_GLOBAL_REGISTER":
                        packed_c['op0'] = registers.COND_GRS[int(c['op0']['name'])]
                    elif c['op0']['type'] == "XTRA_READ_ONLY_REGISTER":
                        packed_c['op0'] = registers.COND_TS
                    elif c['op0']['type'] == "XTRA_PKT_FIELD" or c['op0']['type'] == "XTRA_EVENT_FIELD":
                        print("Not implemented still")
                    else:
                        print("Operand 0 not recognized")

                    if c['op1']['type'] == "XTRA_LOCAL_REGISTER":
                        packed_c['op1'] = registers.COND_LRS[int(c['op1']['name'])]
                    elif c['op1']['type'] == "XTRA_GLOBAL_REGISTER":
                        packed_c['op1'] = registers.COND_GRS[int(c['op1']['name'])]
                    elif c['op1']['type'] == "XTRA_READ_ONLY_REGISTER":
                        packed_c['op1'] = registers.COND_TS
                    elif c['op1']['type'] == "XTRA_INTEGER_CONST":
                        packed_c['op1'] = c['op1']['value']
                        packed_c['const'] = True
                    elif c['op1']['type'] == "XTRA_PKT_FIELD" or c['op1']['type'] == "XTRA_EVENT_FIELD":
                        print("Not implemented still")
                    else:
                        print("Operand 1 not recognized")

                    packed_c['opcode'] = c['opcode']

                packed_conditions.append(packed_c)

                # append the hf list to the global header fields list
                # self.hfs.append(hfs)

            self.conditions.append(packed_conditions)

    def get_conditions_results(self, t, stage):
        tr_results = [FB_DONTCARE for i in range(len(self.conditions[stage]))]

        for c in t[condition_results]:
            if c["result"] == "XTRA_TRUE":
                tr_results[int(c["id"])] = FB_TRUE
            elif c["result"] == "XTRA_FALSE":
                tr_results[int(c["id"])] = FB_FALSE

        return tr_results
