package it.uniroma2.netprog.xtrac.compiler.utils;

import it.uniroma2.netprog.xtrac.exception.AlreadyDefinedException;
import it.uniroma2.netprog.xtrac.exception.UndeclaredStateException;
import it.uniroma2.netprog.xtrac.model.condition.Condition;
import it.uniroma2.netprog.xtrac.model.state.State;
import it.uniroma2.netprog.xtrac.model.table.Table;
import it.uniroma2.netprog.xtrac.model.table.TableRow;
import java.util.Vector;

public class TableFactory {
    private static TableFactory ourInstance = new TableFactory();

    private Table currentTable;

    public static TableFactory getInstance() {
        return ourInstance;
    }

    private TableFactory() {
    }

    public void createTable() {
        currentTable = new Table();
    }

    public void setInitialState(State state) throws AlreadyDefinedException {
        if(currentTable.getInitialState() != null && !currentTable.getInitialState().equals(state))
            throw new AlreadyDefinedException("Multiple definition of the initial state");
        currentTable.setInitialState(state);
    }

    public Table finalizeTableAndGet(Vector<Condition> conditions, Vector<TableRow> tableRows) throws UndeclaredStateException {
        currentTable.setConditions(conditions);
        currentTable.setTableRows(tableRows);

        if(currentTable.getInitialState() == null)
            throw new UndeclaredStateException("Initial state not defined");

        return currentTable;
    }
}
