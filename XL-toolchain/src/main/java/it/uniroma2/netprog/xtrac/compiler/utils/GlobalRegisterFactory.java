package it.uniroma2.netprog.xtrac.compiler.utils;

import it.uniroma2.netprog.xtrac.exception.AlreadyDefinedException;
import it.uniroma2.netprog.xtrac.exception.UndeclaredRegisterException;
import it.uniroma2.netprog.xtrac.model.operand.GlobalRegister;
import java.util.HashMap;

public class GlobalRegisterFactory {
    private static GlobalRegisterFactory ourInstance = new GlobalRegisterFactory();
    private HashMap<String, GlobalRegister> registers;
    private int counter;

    public static GlobalRegisterFactory getInstance() {
        return ourInstance;
    }

    public static void resetInstance() {
        ourInstance = new GlobalRegisterFactory();
    }

    private GlobalRegisterFactory() {
        registers = new HashMap<>();
        counter = 0;
    }

    public GlobalRegister allocateRegister(String name) throws AlreadyDefinedException {
        if(LocalRegisterFactory.getInstance().isPresent(name) || isPresent(name))
            throw new AlreadyDefinedException("The register '"+name+"' was already defined!");

        GlobalRegister register = new GlobalRegister(String.valueOf(counter));
        registers.put(name, register);
        counter++;

        return register;
    }

    public GlobalRegister getRegister(String name) throws UndeclaredRegisterException {
        GlobalRegister register = registers.get(name);
        if(register == null)
            throw new UndeclaredRegisterException("The register '"+name+"' was not defined in this scope");
        return register;
    }

    public boolean isPresent(String name)
    {
        return registers.containsKey(name);
    }
}
