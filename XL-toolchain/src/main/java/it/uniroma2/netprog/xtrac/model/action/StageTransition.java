package it.uniroma2.netprog.xtrac.model.action;

import it.uniroma2.netprog.xtrac.model.opcodes.StageTransitionOpcode;
import it.uniroma2.netprog.xtrac.model.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StageTransition extends Action{
    private Stage stage;

    public StageTransitionOpcode getOpcode() {
        return StageTransitionOpcode.XTRA_STAGE_TRANSITION;
    }
}
