package it.uniroma2.netprog.xtrac.model.eventFieldEnums;

import it.uniroma2.netprog.xtrac.exception.InvalidValueException;

public enum Timeout {
    XTRA_TIMEOUT_DATA0,
    XTRA_TIMEOUT_DATA1;

    public static Timeout toEnum(String string) throws InvalidValueException {
        switch (string) {
            case "data0":
                return XTRA_TIMEOUT_DATA0;
            case "data1":
                return XTRA_TIMEOUT_DATA1;
            default:
                throw new InvalidValueException("Undefined field '" + string + "' for the event timeout");
        }
    }
}
