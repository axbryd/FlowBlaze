package it.uniroma2.netprog.xtrac.model.condition;

import it.uniroma2.netprog.xtrac.model.opcodes.ConditionOpcode;
import it.uniroma2.netprog.xtrac.model.operand.Operand;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class Condition {
    @EqualsAndHashCode.Exclude private int id;
    private Operand op0;
    private Operand op1;
    private ConditionOpcode opcode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Condition condition = (Condition) o;
        return Objects.equals(op0, condition.op0) &&
                Objects.equals(op1, condition.op1) &&
                opcode == condition.opcode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(op0, op1, opcode);
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
