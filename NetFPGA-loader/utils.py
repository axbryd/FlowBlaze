def isAnUpdate(opcode):
    # TODO: add all the opcodes
    return opcode == "XTRA_SUM" or opcode == "XTRA_MINUS" or opcode == "XTRA_MULTIPLY" or opcode == "XTRA_DIVIDE"


def hton(value, mask):
    if mask < 0:
        print("Error handling negative value")
    elif mask == 0xffffff00:
        return value
    elif mask == 0xffff0000:
        return ((value & 0xff) << 8) | ((value & 0xff00) >> 8)
    elif mask == 0:
        return ((value & 0x000000ff) << 24) | ((value & 0xff00) << 8) | \
                 ((value & 0xff0000) >> 8) | ((value & 0xff000000) >> 24)
    else:
        print("Error not a valid mask")
        return 0
