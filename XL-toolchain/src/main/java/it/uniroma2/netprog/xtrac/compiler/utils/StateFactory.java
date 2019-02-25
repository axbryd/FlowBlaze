package it.uniroma2.netprog.xtrac.compiler.utils;

import it.uniroma2.netprog.xtrac.exception.UndeclaredStateException;
import it.uniroma2.netprog.xtrac.model.state.State;
import lombok.AccessLevel;
import lombok.Getter;
import java.util.HashMap;

@Getter
public class StateFactory {
    // TODO: implement per stage state generation
    @Getter(AccessLevel.PRIVATE)
    private static StateFactory ourInstance = new StateFactory();
    private int counter;
    private HashMap<String, State> states;
    private HashMap<String, State> pendingStates; // states referenced not declared yet
    @Getter(AccessLevel.PUBLIC)
    private State lastDeclaredState; // The last state being declared

    public static StateFactory getInstance() {
        return ourInstance;
    }

    private StateFactory() {
        states = new HashMap<>();
        pendingStates = new HashMap<>();
        counter = 0;

        // TODO: doc it && check max num of states
        states.put("any", new State(255));
    }

    public State declareState(String name)
    {
        State state = states.get(name);
        if (state == null)
        {
            state = new State(counter);
            states.put(name, state);
            counter++;

        }
        pendingStates.remove(name);
        lastDeclaredState = state;
        return state;
    }

    public State referenceState(String name)
    {
        State state = states.get(name);
        if (state == null)
        {
            state = new State(counter);
            states.put(name, state);
            pendingStates.put(name, state);
            counter++;
        }
        return state;
    }

    public void checkPendings() throws UndeclaredStateException {
        if(!pendingStates.isEmpty()) {
            String states = new String();
            for (String name : pendingStates.keySet()) {
                states += name+", ";
            }
            if(pendingStates.keySet().size() > 1)
                throw new UndeclaredStateException("The state "+states+"wasn't declared!");
            else
                throw new UndeclaredStateException("The states "+states+"weren't declared!");
        }
    }

    public String getStateName(State state) {
        for (String o: states.keySet()) {
            if (states.get(o).equals(state)) {
                return o;
            }
        }
        return null;
    }
}
