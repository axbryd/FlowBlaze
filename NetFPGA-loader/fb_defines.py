FB_TRUE = 1
FB_FALSE = 0
FB_DONTCARE = 2
FB_EXACT = 3  # only to say that we have to apply exact matching

# actions
FB_SENDPKT = "XTRA_SENDPKT"
FB_SETFIELD = "XTRA_SETFIELD"

# conditions
FB_LESS = "XTRA_LESS"
FB_LESS_EQ = "XTRA_LESS_EQ"
FB_GREATER = "XTRA_GREATER"
FB_GREATER_EQ = "XTRA_GREATER_EQ"
FB_EQUAL = "XTRA_EQUAL"
FB_NOT_EQUAL = "XTRA_NOT_EQUAL"

# opcodes
FB_SUM = "XTRA_SUM"
FB_MINUS = "XTRA_MINUS"
FB_MULTIPLY = "XTRA_MULTIPLY"
FB_DIVIDE = "XTRA_DIVIDE"
FB_MODULO = "XTRA_MODULO"
FB_MAX = "XTRA_MAX"
FB_MIN = "XTRA_MIN"

RED = "\033[1;31;0m"
RESET = "\033[1;37;0m"


def mask_from_field(field):
    if field['type'] == "XTRA_EVENT_FIELD":
        return 0xffffff00

    length = int(field['length'])
    mask = 0

    if length == 4:
        mask = 0x0
    elif length == 3:
        mask = 0xff000000
    elif length == 2:
        mask = 0xffff0000
    elif length == 1:
        mask = 0xffffff00
    else:
        print("error: field len not supported")
        exit(-1)

    return mask


def find_hf_position(hfs, field):
    if field in hfs:
        return hfs.index(field)

    else:
        print("Field not found")
        return 0
