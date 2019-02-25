package it.uniroma2.netprog.xtrac.compilerStateMachine;

import it.uniroma2.netprog.xtrac.exception.AlreadyDefinedException;
import it.uniroma2.netprog.xtrac.exception.IllegalCompilerTransitionException;
import it.uniroma2.netprog.xtrac.exception.UndeclaredStateException;
import it.uniroma2.netprog.xtrac.exception.UndefinedMacroActionException;
import it.uniroma2.netprog.xtrac.model.action.Action;
import it.uniroma2.netprog.xtrac.model.event.Event;
import it.uniroma2.netprog.xtrac.model.state.State;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import java.util.Vector;

@Getter
@Setter
public class CompilerStateMachine {
    @Setter(AccessLevel.PRIVATE)
    private static CompilerStateMachine instance = new CompilerStateMachine();


    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private CompilerState compilerState;
    @Setter(AccessLevel.PUBLIC)
    private boolean isInitial = false; // The set state is the initial one
    private Event currentEvent;
    private State currentState;
    private String currentMacroAction; // The name of the current Macro action block
    private String currentStageName; // The name of the current stage block


    private CompilerStateMachine() {
        InStageBlock inStageBlock = new InStageBlock();
        InStateBlock inStateBlock = new InStateBlock();
        InEventBlock inEventBlock = new InEventBlock();
        InEventSequentialBlock inEventSequentialBlock = new InEventSequentialBlock();
        InConditionBlock inConditionBlock = new InConditionBlock();
        InConditionSequentialBlock inConditionSequentialBlock = new InConditionSequentialBlock();
        InStateSequentialBlock inStateSequentialBlock = new InStateSequentialBlock();
        InOutsideBlock1 inOutsideBlock1 = new InOutsideBlock1();
        InOutsideBlock2 inOutsideBlock2 = new InOutsideBlock2();
        InMacroActionDefinitionBlock inMacroActionDefinitionBlock = new InMacroActionDefinitionBlock();
        InMacroActionSequentialBlock inMacroActionSequentialBlock = new InMacroActionSequentialBlock();

        // Defining legal transitions per compilerState
        inMacroActionDefinitionBlock.setOutsideBlock(inStageBlock);
        inMacroActionDefinitionBlock.setActionBlock(inMacroActionSequentialBlock);

        inMacroActionSequentialBlock.setOutsideBlock(inMacroActionDefinitionBlock);

        inOutsideBlock1.setInsideBlock(inStageBlock);

        inStageBlock.setOutsideBlock(inOutsideBlock2);
        inStageBlock.setInsideBlock(inStateBlock);
        inStageBlock.setActionBlock(inMacroActionDefinitionBlock);

        inStateBlock.setOutsideBlock(inStageBlock);
        inStateBlock.setInsideBlock(inEventBlock);
        inStateBlock.setActionBlock(inStateSequentialBlock);

        inEventBlock.setInsideBlock(inConditionBlock);
        inEventBlock.setOutsideBlock(inStateBlock);
        inEventBlock.setActionBlock(inEventSequentialBlock);

        inConditionBlock.setOutsideBlock(inEventBlock);
        inConditionBlock.setActionBlock(inConditionSequentialBlock);

        inOutsideBlock2.setInsideBlock(inStageBlock);

        inStateSequentialBlock.setOutsideBlock(inStateBlock);

        inEventSequentialBlock.setOutsideBlock(inEventBlock);

        inConditionSequentialBlock.setOutsideBlock(inConditionBlock);

        compilerState = inOutsideBlock1; // initial compilerState
    }

    private String getStateName() {
        return compilerState.getClass().getName();
    }

    public static CompilerStateMachine getInstance() {
        return instance;
    }

    public void addAction(Action action) {
        compilerState.addAction(action, currentState, currentEvent, currentMacroAction);
    }

    public void addActions(Vector<Action> actions) {
        compilerState.addActions(actions, currentState, currentEvent, currentMacroAction);
    }

    public void goInsideActionsBlock() throws IllegalCompilerTransitionException, AlreadyDefinedException {
        String oldState = getStateName();
        compilerState.notifyGoInsideActionsBlockBeforeStateTransition(currentState, isInitial, currentEvent, currentStageName, currentMacroAction);
        compilerState = compilerState.getActionBlock();
        if(compilerState == null)
            throw new IllegalCompilerTransitionException("Illegal goInsideActionsBlock transition from state '"+oldState+"' to '"+getStateName()+"'");
        compilerState.notifyGoInsideActionsBlockAfterStateTransition(currentState, isInitial, currentEvent, currentStageName, currentMacroAction);
    }

    public void goInsideBlock() throws IllegalCompilerTransitionException, AlreadyDefinedException {
        String oldState = getStateName();
        compilerState.notifyGoInsideBlockBeforeStateTransition(currentState, isInitial, currentEvent, currentStageName, currentMacroAction);
        compilerState = compilerState.getInsideBlock();
        if(compilerState == null)
            throw new IllegalCompilerTransitionException("Illegal goInsideActionsBlock transition from state '"+oldState+"' to '"+getStateName()+"'");
        compilerState.notifyGoInsideBlockAfterStateTransition(currentState, isInitial, currentEvent, currentStageName, currentMacroAction);
    }

    public void goOutsideBlock() throws IllegalCompilerTransitionException, UndefinedMacroActionException, UndeclaredStateException, AlreadyDefinedException {
        String oldState = getStateName();
        compilerState.notifyGoOutsideBlockBeforeStateTransition(currentState, isInitial, currentEvent, currentStageName, currentMacroAction);
        compilerState = compilerState.getOutsideBlock();
        if(compilerState == null)
            throw new IllegalCompilerTransitionException("Illegal goInsideActionsBlock transition from state '"+oldState+"' to '"+getStateName()+"'");
        compilerState.notifyGoOutsideBlockAfterStateTransition(currentState, isInitial, currentEvent, currentStageName, currentMacroAction);
    }

}
