package it.uniroma2.netprog.xtrac.model.operand;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public abstract class Register implements Operand {
    private String name;

    @Override
    public String toString() {
        return name;
    }
}
