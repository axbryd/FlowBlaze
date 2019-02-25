package it.uniroma2.netprog.xtrac.serializers;

import com.google.gson.*;
import it.uniroma2.netprog.xtrac.model.operand.PacketField;
import it.uniroma2.netprog.xtrac.model.stage.Stage;
import it.uniroma2.netprog.xtrac.model.table.Table;
import java.lang.reflect.Type;

public class StageSerializer implements JsonSerializer {
    @Override
    public JsonElement serialize(Object o, Type type, JsonSerializationContext jsonSerializationContext) {
        final JsonObject jsonObject = new JsonObject();
        Stage stage = (Stage) o;

        Gson standardGson = new Gson();

        final GsonBuilder tableBuilder = new GsonBuilder();
        tableBuilder.registerTypeAdapter(Table.class, new TableSerializer());
        final Gson tableGson = tableBuilder.create();

        String tableString = tableGson.toJson(stage.getTable());
        jsonObject.add("table", standardGson.fromJson(tableString, JsonObject.class));

        jsonObject.addProperty("stage_label", String.valueOf(stage.getLabel()));

        final GsonBuilder packetFieldBuilder = new GsonBuilder();
        packetFieldBuilder.registerTypeAdapter(PacketField.class, new OperandSerializer());
        final Gson packetFieldGson = packetFieldBuilder.create();

        String flowKeyString = packetFieldGson.toJson(stage.getFlowKey().getHeaderFields());
        JsonArray jsonArray = standardGson.fromJson(flowKeyString, JsonArray.class);
        jsonObject.add("flow_key", jsonArray);

        String updateKeyString = packetFieldGson.toJson(stage.getUpdateKey().getHeaderFields());
        jsonArray = standardGson.fromJson(updateKeyString, JsonArray.class);
        jsonObject.add("update_key", jsonArray);

        return jsonObject;
    }
}
