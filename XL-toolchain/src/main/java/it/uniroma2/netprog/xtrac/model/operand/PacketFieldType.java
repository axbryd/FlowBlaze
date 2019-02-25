package it.uniroma2.netprog.xtrac.model.operand;

import it.uniroma2.netprog.xtrac.exception.UnknownOpcodeException;

public enum PacketFieldType {
    XTRA_PKT_METADATA,
    XTRA_PKT_DATA;

    public static PacketFieldType toEnum(String string) throws UnknownOpcodeException {
        switch (string) {
            case "packet.data":
                return XTRA_PKT_DATA;
            case "packet.metadata":
                return XTRA_PKT_METADATA;
            default:
                throw new UnknownOpcodeException("The symbol '"+string+"' wasn't defined");
        }
    }
}
