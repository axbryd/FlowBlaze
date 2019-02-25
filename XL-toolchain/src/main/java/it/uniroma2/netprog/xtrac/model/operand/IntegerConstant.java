package it.uniroma2.netprog.xtrac.model.operand;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntegerConstant extends Constant {
    public IntegerConstant(String string) {
        super(string);
    }
}
