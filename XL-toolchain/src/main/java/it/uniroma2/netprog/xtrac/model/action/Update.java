package it.uniroma2.netprog.xtrac.model.action;

import it.uniroma2.netprog.xtrac.model.opcodes.UpdateOpcode;
import it.uniroma2.netprog.xtrac.model.operand.Operand;
import it.uniroma2.netprog.xtrac.model.operand.Register;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Update extends SequentializableActions {
    private UpdateOpcode opcode;
    private Operand op0;
    private Operand op1;
    private Register op2;
}
