package it.uniroma2.netprog.xtrac.model.action;

import it.uniroma2.netprog.xtrac.model.opcodes.StateTransitionOpcode;
import it.uniroma2.netprog.xtrac.model.state.State;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class StateTransition extends Action {
    private State op0;

    public StateTransitionOpcode getOpcode() {
        return StateTransitionOpcode.XTRA_STATE_TRANSITION;
    }
}
