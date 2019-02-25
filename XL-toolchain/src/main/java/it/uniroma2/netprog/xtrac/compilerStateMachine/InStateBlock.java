package it.uniroma2.netprog.xtrac.compilerStateMachine;

import it.uniroma2.netprog.xtrac.compiler.utils.StateFactory;
import it.uniroma2.netprog.xtrac.compiler.utils.TableFactory;
import it.uniroma2.netprog.xtrac.compiler.utils.TableRowFactory;
import it.uniroma2.netprog.xtrac.exception.AlreadyDefinedException;
import it.uniroma2.netprog.xtrac.exception.UndefinedMacroActionException;
import it.uniroma2.netprog.xtrac.model.action.Action;
import it.uniroma2.netprog.xtrac.model.event.Event;
import it.uniroma2.netprog.xtrac.model.state.State;

import java.util.Vector;

class InStateBlock extends CompilerState {

    @Override
    public void notifyGoInsideBlockBeforeStateTransition(State state, boolean isInitial, Event event, String stageName, String macroActionName) {

    }

    @Override
    public void notifyGoInsideBlockAfterStateTransition(State state, boolean isInitial, Event event, String stageName, String macroActionName) throws AlreadyDefinedException {
        TableRowFactory.getInstance().addState(state);
        if(isInitial)
            TableFactory.getInstance().setInitialState(StateFactory.getInstance().getLastDeclaredState());
    }

    @Override
    public void notifyGoOutsideBlockBeforeStateTransition(State state, boolean isInitial, Event event, String stageName, String macroActionName) throws UndefinedMacroActionException, AlreadyDefinedException {
        TableRowFactory.getInstance().finalizeTableRows();
    }

    @Override
    public void notifyGoOutsideBlockAfterStateTransition(State state, boolean isInitial, Event event, String stageName, String macroActionName) {

    }

    @Override
    public void notifyGoInsideActionsBlockBeforeStateTransition(State state, boolean isInitial, Event event, String stageName, String macroActionName) {

    }

    @Override
    public void notifyGoInsideActionsBlockAfterStateTransition(State state, boolean isInitial, Event event, String stageName, String macroActionName) {

    }

    @Override
    public void addAction(Action action, State currentState, Event currentEvent, String currentMacroAction) {
        TableRowFactory.getInstance().addActionToBlock(action, currentState);
    }

    @Override
    public void addActions(Vector<Action> actions, State currentState, Event currentEvent, String currentMacroAction) {
        for (Action action: actions)
            addAction(action, currentState, currentEvent, currentMacroAction);
    }
}
