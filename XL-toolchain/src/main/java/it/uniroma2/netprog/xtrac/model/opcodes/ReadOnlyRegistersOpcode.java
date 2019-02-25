package it.uniroma2.netprog.xtrac.model.opcodes;

import it.uniroma2.netprog.xtrac.exception.UndeclaredRegisterException;

public enum ReadOnlyRegistersOpcode {
    XTRA_TIMESTAMP;

    public static ReadOnlyRegistersOpcode toEnum(String string) throws UndeclaredRegisterException {
        switch (string){
            case "currentTime":
                return XTRA_TIMESTAMP;
            default:
                throw new UndeclaredRegisterException("The ReadOnlyRegister '"+string+"' doesn't exist!");
        }
    }
}
