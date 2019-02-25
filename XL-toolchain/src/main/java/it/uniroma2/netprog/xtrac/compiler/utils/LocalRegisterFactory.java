package it.uniroma2.netprog.xtrac.compiler.utils;

import it.uniroma2.netprog.xtrac.exception.AlreadyDefinedException;
import it.uniroma2.netprog.xtrac.exception.UndeclaredRegisterException;
import it.uniroma2.netprog.xtrac.model.operand.LocalRegister;
import java.util.HashMap;

public class LocalRegisterFactory {
    private static LocalRegisterFactory ourInstance = new LocalRegisterFactory();
    private HashMap<String, LocalRegister> registers;
    private int counter;

    public static LocalRegisterFactory getInstance() {
        return ourInstance;
    }

    public static void resetInstance() {
        ourInstance = new LocalRegisterFactory();
    }

    private LocalRegisterFactory() {
        registers = new HashMap<>();
        counter = 0;
    }

    public LocalRegister allocateRegister(String name) throws AlreadyDefinedException {
        if(GlobalRegisterFactory.getInstance().isPresent(name) || isPresent(name))
            throw new AlreadyDefinedException("The register '"+name+"' was already defined!");

        LocalRegister register = new LocalRegister(String.valueOf(counter));
        registers.put(name, register);
        counter++;

        return register;
    }

    public LocalRegister getRegister(String name) throws UndeclaredRegisterException {
        LocalRegister register = registers.get(name);
        if(register == null)
            throw new UndeclaredRegisterException("The register '"+name+"' was not defined in this scope");
        return register;
    }

    public boolean isPresent(String name)
    {
        return registers.containsKey(name);
    }
}
