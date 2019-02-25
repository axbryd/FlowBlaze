package it.uniroma2.netprog.xtrac.compilerStateMachine;

import it.uniroma2.netprog.xtrac.compiler.utils.*;
import it.uniroma2.netprog.xtrac.exception.AlreadyDefinedException;
import it.uniroma2.netprog.xtrac.exception.UndeclaredStateException;
import it.uniroma2.netprog.xtrac.exception.UndefinedMacroActionException;
import it.uniroma2.netprog.xtrac.model.action.Action;
import it.uniroma2.netprog.xtrac.model.condition.Condition;
import it.uniroma2.netprog.xtrac.model.event.Event;
import it.uniroma2.netprog.xtrac.model.state.State;
import it.uniroma2.netprog.xtrac.model.table.TableRow;

import java.util.Vector;

class InStageBlock extends CompilerState {

    @Override
    public void notifyGoInsideBlockBeforeStateTransition(State state, boolean isInitial, Event event, String stageName, String macroActionName) {

    }

    @Override
    public void notifyGoInsideBlockAfterStateTransition(State state, boolean isInitial, Event event, String stageName, String macroActionName) throws AlreadyDefinedException {
        StageFactory.getInstance().declareStage(stageName);
        TableFactory.getInstance().createTable();
    }

    @Override
    public void notifyGoOutsideBlockBeforeStateTransition(State state, boolean isInitial, Event event, String stageName, String macroActionName) throws UndefinedMacroActionException, UndeclaredStateException, AlreadyDefinedException {
        Vector<TableRow> tableRows = TableRowFactory.getInstance().getTableRowsAndClear();
        Vector<Condition> conditions = ConditionFactory.getInstance().getConditionsAndClear();
        StageFactory.getInstance().getLastDeclaredStage().setTable(TableFactory.getInstance().finalizeTableAndGet(conditions, tableRows));
        MacroActionsFactory.resetInstance();
        LocalRegisterFactory.resetInstance();
        GlobalRegisterFactory.resetInstance();
    }

    @Override
    public void notifyGoOutsideBlockAfterStateTransition(State state, boolean isInitial, Event event, String stageName, String macroActionName) {

    }

    @Override
    public void notifyGoInsideActionsBlockBeforeStateTransition(State state, boolean isInitial, Event event, String stageName, String macroActionName) throws AlreadyDefinedException {
    }

    @Override
    public void notifyGoInsideActionsBlockAfterStateTransition(State state, boolean isInitial, Event event, String stageName, String macroActionName) {

    }

    @Override
    public void addAction(Action action, State currentState, Event currentEvent, String currentMacroAction) {

    }

    @Override
    public void addActions(Vector<Action> actions, State currentState, Event currentEvent, String currentMacroAction) {

    }
}
