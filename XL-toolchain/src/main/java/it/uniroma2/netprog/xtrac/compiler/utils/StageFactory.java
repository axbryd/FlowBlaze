package it.uniroma2.netprog.xtrac.compiler.utils;

import it.uniroma2.netprog.xtrac.exception.UndeclaredStateException;
import it.uniroma2.netprog.xtrac.exception.UndefinedFlowKeyException;
import it.uniroma2.netprog.xtrac.model.stage.Stage;
import it.uniroma2.netprog.xtrac.model.state.State;
import lombok.AccessLevel;
import lombok.Getter;
import java.util.HashMap;
import java.util.Vector;

public class StageFactory {
    // TODO: implement per stage state generation
    @Getter(AccessLevel.PRIVATE) private static StageFactory ourInstance = new StageFactory();
    @Getter(AccessLevel.PRIVATE) private HashMap<String, Stage> stages;
    @Getter(AccessLevel.PRIVATE) private HashMap<String, Stage> pendingStages; // stages referenced not declared yet
    @Getter(AccessLevel.PRIVATE) private int counter;
    @Getter(AccessLevel.PUBLIC)  private Stage lastDeclaredStage; // reference to the last state declared

    public static StageFactory getInstance() {
        return ourInstance;
    }

    private StageFactory() {
        stages = new HashMap<>();
        pendingStages = new HashMap<>();
        counter = 0;
    }

    public Stage declareStage(String name)
    {
        Stage stage = stages.get(name);
        if (stage == null)
        {
            stage = new Stage(counter);

            stages.put(name, stage);
            counter++;
        }
        pendingStages.remove(name);
        lastDeclaredStage = stage;
        return stage;
    }

    public Stage referenceStage(String name)
    {
        Stage stage = stages.get(name);
        if (stage == null)
        {
            stage = new Stage(counter);
            stages.put(name, stage);
            pendingStages.put(name, stage);
            counter++;
        }
        return stage;
    }

    private void checkPendings() throws UndeclaredStateException {
        if(!pendingStages.isEmpty()) {
            String stages = new String();
            for (String name : pendingStages.keySet()) {
                stages += name+", ";
            }
            if(pendingStages.keySet().size() == 1)
                throw new UndeclaredStateException("The stage "+stages.substring(0, stages.length()-2)+" wasn't declared!");
            else
                throw new UndeclaredStateException("The stages "+stages.substring(0, stages.length()-2)+" weren't declared!");
        }
    }

    public Vector<Stage> getStages() throws UndefinedFlowKeyException, UndeclaredStateException {
        checkPendings();
        Vector<Stage> stagesVector = new Vector<>();
        for (String stageName: stages.keySet()) {
            Stage stage = stages.get(stageName);
            if (stage.getFlowKey() != null) {
                stagesVector.add(stage);
            } else
                throw new UndefinedFlowKeyException("Flowkey unset for the stage '"+stageName+"'");
        }
        return stagesVector;
    }

    public String getStageName(Stage stage) {
        for (String o: stages.keySet()) {
            if (stages.get(o).equals(stage)) {
                return o;
            }
        }
        return null;
    }
}