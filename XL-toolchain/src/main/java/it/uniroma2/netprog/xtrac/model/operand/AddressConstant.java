package it.uniroma2.netprog.xtrac.model.operand;

import lombok.EqualsAndHashCode;

public abstract class AddressConstant extends Constant {
    public AddressConstant(String value) {
        super(value);
    }
}
