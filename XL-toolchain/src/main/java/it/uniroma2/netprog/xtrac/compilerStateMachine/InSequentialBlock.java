package it.uniroma2.netprog.xtrac.compilerStateMachine;

import it.uniroma2.netprog.xtrac.compiler.utils.SequentialActionsFactory;
import it.uniroma2.netprog.xtrac.model.action.Action;
import it.uniroma2.netprog.xtrac.model.action.SequentializableActions;
import it.uniroma2.netprog.xtrac.model.event.Event;
import it.uniroma2.netprog.xtrac.model.state.State;

import java.util.Vector;

abstract class InSequentialBlock extends CompilerState {
    @Override
    public void addAction(Action action, State currentState, Event currentEvent, String currentMacroAction) {
        SequentialActionsFactory.getInstance().addActionToSequentialActionsBlock((SequentializableActions) action);
    }

    @Override
    public void addActions(Vector<Action> actions, State currentState, Event currentEvent, String currentMacroAction) {
        for (Action action: actions)
            SequentialActionsFactory.getInstance().addActionToSequentialActionsBlock((SequentializableActions) action);
    }
}
