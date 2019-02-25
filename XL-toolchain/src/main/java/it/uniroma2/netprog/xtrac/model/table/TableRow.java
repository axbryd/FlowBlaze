package it.uniroma2.netprog.xtrac.model.table;

import it.uniroma2.netprog.xtrac.compiler.utils.StageFactory;
import it.uniroma2.netprog.xtrac.compiler.utils.StateFactory;
import it.uniroma2.netprog.xtrac.exception.AlreadyDefinedException;
import it.uniroma2.netprog.xtrac.model.action.Action;
import it.uniroma2.netprog.xtrac.model.condition.ConditionResult;
import it.uniroma2.netprog.xtrac.model.event.Event;
import it.uniroma2.netprog.xtrac.model.stage.Stage;
import it.uniroma2.netprog.xtrac.model.state.State;
import lombok.Getter;
import lombok.Setter;
import java.util.Vector;

@Getter
@Setter
public class TableRow {
    private Vector<ConditionResult> conditionResults;
    private Vector<Action> actions;
    private State state;
    private Event event;
    private State nextState;
    private Stage nextStage;

    public TableRow(State state, Event event, Stage nextStage) {
        this.state = state;
        this.event = event;
        this.conditionResults = new Vector<>();
        this.actions = new Vector<>();
        this.nextState = state;
        this.nextStage = nextStage;
    }

    public void setNextState(State nextState) throws AlreadyDefinedException {
        if(!this.nextState.equals(state))
            throw new AlreadyDefinedException("Multiple call of the setNextState function for the same state transition (event="+Event.getNameFromEnum(event)+", state="+
                                               StateFactory.getInstance().getStateName(state)+", next_state="+StateFactory.getInstance().getStateName(nextState)+
                                                ", next_stage="+ StageFactory.getInstance().getStageName(nextStage)+")");
        this.nextState = nextState;
    }

    public boolean isEmpty() {
        return actions.isEmpty() && state.equals(nextState) && nextStage.equals(StageFactory.getInstance().getLastDeclaredStage());
    }
}
