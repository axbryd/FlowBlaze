package it.uniroma2.netprog.xtrac.model.operand;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public abstract class Constant implements Operand {
    private String value;

    @Override
    public String toString() {
        return value;
    }
}
