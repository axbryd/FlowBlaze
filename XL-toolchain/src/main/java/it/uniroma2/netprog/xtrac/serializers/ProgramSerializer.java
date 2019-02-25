package it.uniroma2.netprog.xtrac.serializers;

import com.google.gson.*;
import it.uniroma2.netprog.xtrac.model.program.Program;
import it.uniroma2.netprog.xtrac.model.stage.Stage;
import java.lang.reflect.Type;

public class ProgramSerializer implements JsonSerializer {
    @Override
    public JsonElement serialize(Object o, Type type, JsonSerializationContext jsonSerializationContext) {
        final JsonObject jsonObject = new JsonObject();
        Program program = (Program) o;

        Gson standardGson = new Gson();

        final GsonBuilder stageBuilder = new GsonBuilder();
        stageBuilder.registerTypeAdapter(Stage.class, new StageSerializer());
        final Gson stageGson = stageBuilder.create();

        String tableString = stageGson.toJson(program.getStages());
        jsonObject.add("stages", standardGson.fromJson(tableString, JsonArray.class));

        return jsonObject;
    }
}
