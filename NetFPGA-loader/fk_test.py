import programmer
from fb_defines import *
import sys
import argparse
""" Test flow key functionalities """

rwaxi_path = "/home/sal/rwaxi"
serial_dev = "/dev/ttyUSB1"
out_file = "../elfgen.txt"


usage = """USAGE: \n\tpython fk_test.py -m MODE [-p rwaxi_path/serial_path] 
            \t\t MODE can be 2 for simulation
            \t\t 0 for serial programming
            \t\t 1 for rwaxi PCI programming
            \t\t 2 for axi simulation
            \t\t 3 for file output """


parser = argparse.ArgumentParser(description="NetFPGA compiler from JSON", usage=usage)
parser.add_argument('-m', required=True)
parser.add_argument("-p")
args = parser.parse_args(sys.argv[1:])
print(args)

mode = int(args.m)

if mode == programmer.SERIAL_MODE and args.p is not None:
    serial_dev = args.p
elif mode == programmer.RWAXI_MODE and args.p is not None:
    rwaxi_path = args.p
elif mode == programmer.FILE_MODE and args.p is not None:
    out_file = args.p
elif mode == programmer.SIM_MODE and args.p is not None:
    out_file = args.p
elif mode == programmer.USB_MODE and args.p is not None:
    out_file = args.p
elif mode == programmer.BASH_MODE and args.p is not None:
    out_file = args.p


p = programmer.Programmer(mode=mode, rwaxi_path=rwaxi_path, serial_name=serial_dev, out_file_path=out_file)

p.parsed.pack_flow_keys()
p.parsed.pack_update_keys()
p.parsed.pack_conditions()
p.parsed.pack_entries()

print("############ Writing flow key ############\n")
p.write_flow_key(p.parsed.flow_keys[0])
print("############ Writing update key ############\n")
p.write_update_key(p.parsed.update_keys[0])
print(p.parsed.flow_keys)
print(p.parsed.update_keys)
print()

print("############ Writing header fields ############\n")
p.write_header_fields(p.parsed.hfs[0])
print("\n HFS: " + str(p.parsed.hfs))

print("\n########### PACKED CONDITIONS ("+str(len(p.parsed.conditions[0]))+") ###########\n")
print(p.parsed.conditions[0])
print("\n########### WRITING CONDITIONS ###########\n")
p.write_conditions(p.parsed.conditions[0])

#print("\n ENTRIES: " + str(p.parsed.entries))
print("############ Writing TCAM 1 entries ###########\n")
p.write_tcam1_entry()

print("\n############ Writing TCAM 2 entries ###########")
p.write_tcam2(0)
for i, e in enumerate(p.parsed.entries[0]):
    print(RED+"\n########## Writing actions for entry #" + str(i) + RESET)
    p.write_actions(i, e['actions'], int(e['next_state']), int(e['state']))

p.finish()
