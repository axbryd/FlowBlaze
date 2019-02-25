package it.uniroma2.netprog.xtrac.model.operand;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class EventField implements Operand {
    private String name;

    @Override
    public String toString() {
        return name;
    }
}
