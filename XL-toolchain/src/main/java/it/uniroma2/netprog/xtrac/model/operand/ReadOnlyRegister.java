package it.uniroma2.netprog.xtrac.model.operand;

import lombok.EqualsAndHashCode;

public class ReadOnlyRegister extends Register {
    public ReadOnlyRegister(String name) {
        super(name);
    }
}
