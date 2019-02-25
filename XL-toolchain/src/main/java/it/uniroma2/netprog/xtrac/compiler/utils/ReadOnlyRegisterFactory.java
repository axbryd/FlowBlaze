package it.uniroma2.netprog.xtrac.compiler.utils;

import it.uniroma2.netprog.xtrac.exception.UndeclaredRegisterException;
import it.uniroma2.netprog.xtrac.model.opcodes.ReadOnlyRegistersOpcode;
import it.uniroma2.netprog.xtrac.model.operand.ReadOnlyRegister;

public class ReadOnlyRegisterFactory {
    private static ReadOnlyRegisterFactory ourInstance = new ReadOnlyRegisterFactory();

    public static ReadOnlyRegisterFactory getInstance() {
        return ourInstance;
    }

    private ReadOnlyRegisterFactory() {
    }

    public ReadOnlyRegister getReadOnlyRegister(String name) throws UndeclaredRegisterException {
        ReadOnlyRegistersOpcode opcode = ReadOnlyRegistersOpcode.toEnum(name);

        if(opcode == null)
            throw new UndeclaredRegisterException("The ReadOnlyRegister '"+name+"' doesn't exist!");

        return new ReadOnlyRegister(opcode.toString());
    }
}
