package it.uniroma2.netprog.xtrac.compiler.utils;

import it.uniroma2.netprog.xtrac.compilerStateMachine.CompilerStateMachine;
import it.uniroma2.netprog.xtrac.exception.AlreadyDefinedException;
import it.uniroma2.netprog.xtrac.model.action.Action;
import it.uniroma2.netprog.xtrac.model.action.StageTransition;
import it.uniroma2.netprog.xtrac.model.action.StateTransition;
import it.uniroma2.netprog.xtrac.model.condition.ConditionResult;
import it.uniroma2.netprog.xtrac.model.event.Event;
import it.uniroma2.netprog.xtrac.model.stage.Stage;
import it.uniroma2.netprog.xtrac.model.state.State;
import it.uniroma2.netprog.xtrac.model.table.TableRow;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

public class TableRowFactory {
    // This factory helps the creation of a table row

    @NoArgsConstructor
    @Getter
    private class TableRowStateEventActions {
        private Vector<TableRow> tableRows = new Vector<>(); // The table rows associated with the event/state
        private Vector<Action> actions = new Vector<>(); // The actions to add
    }

    private static TableRowFactory ourInstance = new TableRowFactory();
    private Vector<TableRow> tableRows; // the in-compiling table rows
    private TableRow current; // the current in compiling table row
    private HashMap<Event, TableRowStateEventActions> eventRows; // The table rows per event
    private HashMap<State, TableRowStateEventActions> stateRows; // The table rows per state

    public static TableRowFactory getInstance() {
        return ourInstance;
    }

    private TableRowFactory() {
        tableRows = new Vector<>();
        eventRows = new HashMap<>();
        stateRows = new HashMap<>();
    }

    public void addState(State state) {
        if (!stateRows.containsKey(state))
            stateRows.put(state, new TableRowStateEventActions());
    }

    public void addEvent(Event event) {
        if (!eventRows.containsKey(event))
            eventRows.put(event, new TableRowStateEventActions());
    }

    public void createNewTableRow(Event event, State state) {
        // This initialize the creation of a new table-row (call first!)
        current = new TableRow(state, event, new Stage(255));
        tableRows.add(current);

        if (!eventRows.containsKey(event))
            eventRows.put(event, new TableRowStateEventActions());
        TableRowStateEventActions tableRowStateEventActions = eventRows.get(event);
        tableRowStateEventActions.tableRows.add(current);

        if (!stateRows.containsKey(state))
            stateRows.put(state, new TableRowStateEventActions());
        tableRowStateEventActions = stateRows.get(state);
        tableRowStateEventActions.tableRows.add(current);
    }

    public void addConditionResult(ConditionResult conditionResult) {
        // This add a condition result, must be called as second
        assert current != null;

        current.getConditionResults().add(conditionResult);
    }

    public void addActionToCurrentTableRow(Action action) {
        current.getActions().add(action);
    }


    public void addActionToBlock(Action action, State state) {
        TableRowStateEventActions tableRowStateEventActions = stateRows.get(state);

        tableRowStateEventActions.getActions().add(action);
    }

    public void addActionToBlock(Action action, Event event) {
        TableRowStateEventActions tableRowStateEventActions = eventRows.get(event);

        tableRowStateEventActions.getActions().add(action);
    }

    public void finalizeTableRows() throws AlreadyDefinedException {
        finalizeEventRows(eventRows.values());
        finalizeStateRows(stateRows.values());
        eventRows = new HashMap<>();
        stateRows = new HashMap<>();
        processSetNext();
        deleteEmptyTableRows();
    }

    private void deleteEmptyTableRows() {
        Vector<TableRow> newTableRows = new Vector<>();

        for (TableRow tableRow: tableRows)
            if(!tableRow.isEmpty())
                newTableRows.add(tableRow);

        tableRows = newTableRows;
    }

    private void processSetNext() throws AlreadyDefinedException {
        for (TableRow tableRow: tableRows) {
            Vector <Action> actions = new Vector<>();
            for (Action action: tableRow.getActions()) {
                if (action.getClass() == StateTransition.class)
                    tableRow.setNextState(((StateTransition) action).getOp0());
                else if (action.getClass() == StageTransition.class)
                    tableRow.setNextStage(((StageTransition) action).getStage());
                else
                    actions.add(action);
            }
            tableRow.setActions(actions);
        }
    }

    private void finalizeEventRows(Collection<TableRowStateEventActions> values) {
        for (TableRowStateEventActions value : values) {
            if (value.getTableRows().isEmpty()) {
                TableRow tableRow = new TableRow(CompilerStateMachine.getInstance().getCurrentState(), CompilerStateMachine.getInstance().getCurrentEvent(), new Stage(255));
                value.getTableRows().add(tableRow);
                tableRows.add(tableRow);
            }
            finalizeRows(value);
        }
    }

    private void finalizeRows(TableRowStateEventActions value) {
        for (TableRow tableRow: value.getTableRows()) {
            tableRow.getActions().addAll(0, value.getActions());
        }
    }

    private void finalizeStateRows(Collection<TableRowStateEventActions> values) {
        for (TableRowStateEventActions value : values) {
            if (value.getTableRows().isEmpty())
                for(Event event: Event.getEvents()) {
                    TableRow tableRow = new TableRow(CompilerStateMachine.getInstance().getCurrentState(), event, new Stage(255));
                    value.getTableRows().add(tableRow);
                    tableRows.add(tableRow);
                }
            finalizeRows(value);
        }
    }

    public Vector<TableRow> getTableRowsAndClear(){
        // Return the table rows list and clear it
        Vector<TableRow> tableRows = new Vector<>(this.tableRows);
        this.tableRows.clear();
        return tableRows;
    }
}