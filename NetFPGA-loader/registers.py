REG_TEST = 0x80000008
TIMER = 0x8000000C
PAUSE = 0x80000090

# global data variables
GR0 = 0x80000100
GR1 = 0x80000104
GR2 = 0x80000108
GR3 = 0x8000010c

GRS = [GR0, GR1, GR2, GR3]

# header fields
# HFs are offset-length data structures ->  [0-5] offset
#                                           [6-7] len: 0 => 8bit,
#                                                      1 => 16bit,
#                                                      2 => 24bit,
#                                                      3 => 32bit
HF1 = 0x80000060
HF2 = 0x80000070
HF3 = 0x80000080

HFS = [HF1, HF2, HF3]

# flow key offsets
# Offsets ar contiguous 64 bits of packet header, maskable
#       example of flow key: offset1 of 26 bytes; offset2 of 34 bytes (starting at src_port)
#
#                                                [ <- 8 bytes -> ]    [ <- 4 bytes -> ]
#                         pkt--> || eth || ip_h | ip_src | ip_dst || src_port | dst_port ... ||
#                      header--> || eth || ip_h | ip_src | ip_dst || src_port | dst_port | src_if | random ... ||
#
# lookup scope -> [0-5]   offset 1 [in bytes]
#                 [16-21] offset 2 [in bytes]
# mask_lookup  -> [0-127] mask for lookup scope

LOOKUP_SCOPE = 0x80000010
UPDATE_SCOPE = 0x80000020
MASK_LOOKUP = [0x80000030 + i*4 for i in range(0, 4)]
MASK_UPDATE = [0x80000040 + i*4 for i in range(0, 4)]


# tcam 1 -> to get the default state, i.e. FK not in hash table; 8 rows X 128 bits
TCAM1_DEFAULT_MASK = [0xffffffff for i in range(0, 8)]
TCAM1_DATA_IN = 0x80010000

# in bits 6-10 of the last mask resides the TCAM entry address 0x8001003c
TCAM1_MASK = [0x80010004 + i * 4 for i in range(15)]

# tcam 2 -> normal tcam for state machine execution; 64 rows X 120 bits
# | HF3[88-119] | HF2[56-87] | HF1[24-55] | conditions [8-23] | flow_state[0-7] |
TCAM2_DEFAULT_MASK = [0xffffffff for i in range(0, 8)]
TCAM2_DATA_IN = [0x80011000 + i*4 for i in range(4)]
TCAM2_MASK = [0x80011020 + i*4 for i in range(0, 8)]


# ram 1 (r1dp 32x32) -> default state for the keys not in the cuckoo
RAM1 = [0x80020000 + i*4 for i in range(0, 32)]

# ram 2 (r2dp 32x32) -> next state [0-7] and instruction for pipe alu [8-15]
RAM2 = [0x80021000 + i*4 for i in range(0, 32)]

# ram 3 (r3dp 32x32) -> 3 types of actions:
#                       - 0-15  -> table_insert/delete
#                       - 16-23 -> push/modify field
#                       - 24-31 -> select dst interface
RAM3 = [0x80022000 + i*4 for i in range(0, 32)]
HASH_INSERT = 0xff00
HASH_DELETE = 0xffff
PUSH_FIELD = 0x000a0000
MODIFY_FIELD = 0x000b0000
PORT_01 = 0x01
PORT_02 = 0x04
PORT_03 = 0x10
PORT_04 = 0x40
PORTS = [PORT_01, PORT_02, PORT_03, PORT_04]
PORT_FLOOD = 0x55
DROP = 0x00

# ram 4 (r4dp 32x32) -> action values for push/modify fields
#                       - 0-13  -> offset
#                       - 14-15 size (1 is 1B, 2 is 2B, 3 is 4B )
#                       - 16-31 value
RAM4 = [0x80023000 + i*4 for i in range(0, 32)]

""" CONDITIONS """

CR1 = 0x80ffff48
CR2 = 0x80ffff4c
CR3 = 0x80ffff50
CR4 = 0x80ffff54

COND_REG = [CR1, CR2, CR3, CR4]

# crX = [op2->0:7 | op1->8:15 | opcode->16:23 | 0x00]

COND_G0 = 0xa
COND_G1 = 0xb
COND_G2 = 0xc
COND_G3 = 0xd
COND_GRS = [COND_G0, COND_G1, COND_G2, COND_G3]
COND_R1 = 0xe
COND_R2 = 0xf
COND_R3 = 0x1a
COND_LRS = [COND_R1, COND_R2, COND_R3]
COND_O1 = 0x1c
COND_O2 = 0x1d
COND_O3 = 0x1e
COND_TS = 0x1f

# condition opcodes
FB_GREATER      = 0x0a
FB_GREATER_EQ   = 0x0b
FB_LESS         = 0x0c
FB_LESS_EQ      = 0x0d
FB_EQ           = 0x0e
FB_NOT_EQ       = 0x0f


""" ACTIONS """

# PIPEALU 256x512
PIPEALU_SLOTS = 16
PIPEALU = [0x80030000 + i*64 for i in range(0, 256)]

# entry 1 -> ram2: 0x00 00 01 ff
#                            \
#                        pipealu[1] -> | act1 (32bit) |
# 1) aggiungere nei bit 8-15 l'indirizzo PIPEALU[entry_num]
# 2) popolare la pipealu

# opcodes
FB_PLUS = 0x10
FB_MINUS = 0x11
FB_AND = 0x12
FB_OR = 0x13
FB_XOR = 0x14
FB_NOP = 0xff

# select register
HEADER_SIZE = 432  # bits

R1_OFFSET = 0x36
R2_OFFSET = 0x3a
R3_OFFSET = 0x3e

REGS_OFFSET = [R1_OFFSET, R2_OFFSET, R3_OFFSET]

G0_OFFSET = 0x42
G1_OFFSET = 0x46
G2_OFFSET = 0x4a
G3_OFFSET = 0x4e
GRS_OFFSET = [G0_OFFSET, G1_OFFSET, G2_OFFSET, G3_OFFSET]

TS_OFFSET = 0x28

#      0x4872                         48 (high part)   72 (low part)
# ---- first stage  -- | opcode 8bit | imm2 8bit | imm1/reg2 8bit | reg1 8bit |

# ---- second stage -- | opcode 8bit | op2 3bit | op1 3bit |

# ---- third stage  -- | opcode 8bit | op2 3bit | op1 3bit |

# lane A:  s1            s2              s3
#         word 0      word4[:16]       word6[:16]

# lane B:
#         word 1      word4[16:32]     word6[16:32]

# lane C
#         word 2      word5[:16]       word7[:16]

# lane D
#         word 3      word5[16:32]     word7[16:32]
