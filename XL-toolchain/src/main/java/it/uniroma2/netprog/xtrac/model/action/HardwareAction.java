package it.uniroma2.netprog.xtrac.model.action;

import it.uniroma2.netprog.xtrac.model.opcodes.HardwareActionOpcode;
import it.uniroma2.netprog.xtrac.model.operand.Operand;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class HardwareAction extends SequentializableActions {
    private Operand op0;
    private Operand op1;
    private Operand op2;
    private HardwareActionOpcode opcode;

    public HardwareAction(HardwareActionOpcode opcode) {
        this.op0 = null;
        this.op1 = null;
        this.op2 = null;
        this.opcode = opcode;    }

    public HardwareAction(Operand op0, HardwareActionOpcode opcode) {
        this.op0 = op0;
        this.op1 = null;
        this.op2 = null;
        this.opcode = opcode;
    }

    public HardwareAction(Operand op0, Operand op1, HardwareActionOpcode opcode) {
        this.op0 = op0;
        this.op1 = op1;
        this.op2 = null;
        this.opcode = opcode;
    }
}
