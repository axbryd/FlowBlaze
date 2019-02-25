package it.uniroma2.netprog.xtrac.compiler.utils;

import it.uniroma2.netprog.xtrac.model.action.SequentialActions;
import it.uniroma2.netprog.xtrac.model.action.SequentializableActions;

public class SequentialActionsFactory {
    private static SequentialActionsFactory ourInstance = new SequentialActionsFactory();

    private SequentialActions sequentialActions = new SequentialActions();

    public static SequentialActionsFactory getInstance() {
        return ourInstance;
    }

    private SequentialActionsFactory() {
    }

    public void addActionToSequentialActionsBlock(SequentializableActions action) {
        sequentialActions.getActions().add(action);
    }

    public SequentialActions getSequentialActionsBlockAndReset() {
        SequentialActions toReturn = sequentialActions;
        sequentialActions = new SequentialActions();
        return toReturn;
    }
}
