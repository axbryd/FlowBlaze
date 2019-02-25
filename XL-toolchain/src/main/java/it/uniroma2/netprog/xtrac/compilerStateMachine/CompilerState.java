package it.uniroma2.netprog.xtrac.compilerStateMachine;

import it.uniroma2.netprog.xtrac.exception.AlreadyDefinedException;
import it.uniroma2.netprog.xtrac.exception.UndeclaredStateException;
import it.uniroma2.netprog.xtrac.exception.UndefinedMacroActionException;
import it.uniroma2.netprog.xtrac.model.action.Action;
import it.uniroma2.netprog.xtrac.model.event.Event;
import it.uniroma2.netprog.xtrac.model.state.State;
import lombok.Getter;
import lombok.Setter;

import java.util.Vector;

@Getter
@Setter
public abstract class CompilerState {
    CompilerState insideBlock, outsideBlock, actionBlock;

    public abstract void notifyGoInsideBlockBeforeStateTransition(State state, boolean isInitial, Event event, String stageName, String macroActionName);

    public abstract void notifyGoInsideBlockAfterStateTransition(State state, boolean isInitial, Event event, String stageName, String macroActionName) throws AlreadyDefinedException;

    public abstract void notifyGoOutsideBlockBeforeStateTransition(State state, boolean isInitial, Event event, String stageName, String macroActionName) throws UndefinedMacroActionException, UndeclaredStateException, AlreadyDefinedException;

    public abstract void notifyGoOutsideBlockAfterStateTransition(State state, boolean isInitial, Event event, String stageName, String macroActionName);

    public abstract void notifyGoInsideActionsBlockBeforeStateTransition(State state, boolean isInitial, Event event, String stageName, String macroActionName) throws AlreadyDefinedException;

    public abstract void notifyGoInsideActionsBlockAfterStateTransition(State state, boolean isInitial, Event event, String stageName, String macroActionName) throws AlreadyDefinedException;

    public abstract void addAction(Action action, State currentState, Event currentEvent, String currentMacroAction);

    public abstract void addActions(Vector<Action> actions, State currentState, Event currentEvent, String currentMacroAction);
}