package it.uniroma2.netprog.xtrac.model.eventFieldEnums;

import it.uniroma2.netprog.xtrac.exception.InvalidValueException;

public enum PktRcvd {
    XTRA_PKT_RCVD_PORT;

    public static PktRcvd toEnum(String string) throws InvalidValueException {
        switch (string) {
            case "port":
                return XTRA_PKT_RCVD_PORT;
            default:
                    throw new InvalidValueException("Undefined field '"+string+"' for the event pktRcvd");
        }
    }
}
