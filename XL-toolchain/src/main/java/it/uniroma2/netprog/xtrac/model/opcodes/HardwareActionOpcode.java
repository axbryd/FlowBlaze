package it.uniroma2.netprog.xtrac.model.opcodes;

import it.uniroma2.netprog.xtrac.exception.UnknownOpcodeException;

public enum HardwareActionOpcode implements Opcode {
    XTRA_SENDPKT,
    XTRA_SETTIMER,
    XTRA_STOREPKT,
    XTRA_SETFIELD,
    XTRA_DELETEPKT,
    XTRA_DELETE_INSTANCE;

    public static HardwareActionOpcode toHardwareActionOpcode(String string) throws UnknownOpcodeException {
        switch (string)
        {
            case "sendPacket":
                return XTRA_SENDPKT;

            case "setTimer":
                return XTRA_SETTIMER;

            case "storePacket":
                return XTRA_STOREPKT;

            case "setField":
                return XTRA_SETFIELD;

            case "deletePacket":
                return XTRA_DELETEPKT;

            case "deleteInstance":
                return XTRA_DELETE_INSTANCE;

            default:
                throw new UnknownOpcodeException("The action "+string+" wasn't declared.");

        }
    }
}

