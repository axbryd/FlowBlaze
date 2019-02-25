package it.uniroma2.netprog.xtrac.serializers;

import com.google.gson.*;
import it.uniroma2.netprog.xtrac.model.action.HardwareAction;
import it.uniroma2.netprog.xtrac.model.action.StateTransition;
import it.uniroma2.netprog.xtrac.model.action.Update;
import it.uniroma2.netprog.xtrac.model.condition.ConditionResult;
import it.uniroma2.netprog.xtrac.model.table.TableRow;
import java.lang.reflect.Type;

public class TableRowSerializer implements JsonSerializer {

    @Override
    public JsonElement serialize(Object o, Type type, JsonSerializationContext jsonSerializationContext) {
        final JsonObject jsonObject = new JsonObject();
        TableRow tableRow = (TableRow) o;

        Gson standardGson = new Gson();

        final GsonBuilder conditionResultBuilder = new GsonBuilder();
        conditionResultBuilder.registerTypeAdapter(ConditionResult.class, new ConditionResultSerializer());
        final Gson conditionResultGson = conditionResultBuilder.create();

        String conditionResultString = conditionResultGson.toJson(tableRow.getConditionResults());
        JsonArray jsonArray = standardGson.fromJson(conditionResultString, JsonArray.class);
        jsonObject.add("condition_results", jsonArray);

        jsonObject.addProperty("state", tableRow.getState().toString());
        jsonObject.addProperty("event", tableRow.getEvent().toString());
        jsonObject.addProperty("next_state", tableRow.getNextState().toString());

        final GsonBuilder actionsSerializer = new GsonBuilder();
        actionsSerializer.registerTypeAdapter(HardwareAction.class, new ActionSerializer());
        actionsSerializer.registerTypeAdapter(Update.class, new ActionSerializer());

        final Gson actionGson = actionsSerializer.create();

        String actionsString = actionGson.toJson(tableRow.getActions());
        jsonArray = standardGson.fromJson(actionsString, JsonArray.class);
        jsonObject.add("actions", jsonArray);

        jsonObject.addProperty("next_stage", String.valueOf(tableRow.getNextStage().getLabel()));

        return jsonObject;
    }
}
