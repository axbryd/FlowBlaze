#!/usr/bin/env python3

from scapy.all import *
import time

PORTS_LIST = [2570, 2827, 3084, 5555]

for cnt, port in enumerate(PORTS_LIST):
    pkt = Ether(src="b4:96:91:15:41:d6", dst="aa:aa:aa:aa:aa:aa")/IP(src="172.16.25.1",dst="172.16.25.2")/TCP(sport=6666, dport=port)
    sendp(pkt, iface="eth3")

    if cnt != len(PORTS_LIST) - 1:
        time.sleep(1)

