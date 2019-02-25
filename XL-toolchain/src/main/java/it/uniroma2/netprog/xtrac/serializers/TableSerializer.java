package it.uniroma2.netprog.xtrac.serializers;

import com.google.gson.*;
import it.uniroma2.netprog.xtrac.model.condition.Condition;
import it.uniroma2.netprog.xtrac.model.table.Table;
import it.uniroma2.netprog.xtrac.model.table.TableRow;
import java.lang.reflect.Type;

public class TableSerializer implements JsonSerializer {
    @Override
    public JsonElement serialize(Object o, Type type, JsonSerializationContext jsonSerializationContext) {
        final JsonObject jsonObject = new JsonObject();
        Table table = (Table) o;

        Gson standardGson = new Gson();

        final GsonBuilder tableRowBuilder = new GsonBuilder();
        tableRowBuilder.registerTypeAdapter(TableRow.class, new TableRowSerializer());
        final Gson tableRowGson = tableRowBuilder.create();

        String tableRowsString = tableRowGson.toJson(table.getTableRows());
        JsonArray jsonArray = standardGson.fromJson(tableRowsString, JsonArray.class);

        jsonObject.add("table_rows", jsonArray);

        jsonObject.addProperty("initial_state", table.getInitialState().toString());

        final GsonBuilder conditionBuilder = new GsonBuilder();
        conditionBuilder.registerTypeAdapter(Condition.class, new ConditionSerializer());
        final Gson conditionGson = conditionBuilder.create();

        String conditionsString = conditionGson.toJson(table.getConditions());
        jsonArray = standardGson.fromJson(conditionsString, JsonArray.class);
        jsonObject.add("conditions", jsonArray);

        return jsonObject;
    }
}
