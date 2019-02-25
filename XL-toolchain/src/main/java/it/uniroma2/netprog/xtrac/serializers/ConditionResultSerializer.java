package it.uniroma2.netprog.xtrac.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.uniroma2.netprog.xtrac.model.condition.ConditionResult;

import java.lang.reflect.Type;

public class ConditionResultSerializer implements JsonSerializer {

    @Override
    public JsonElement serialize(Object o, Type type, JsonSerializationContext jsonSerializationContext) {
        final JsonObject jsonObject = new JsonObject();
        ConditionResult conditionResult = (ConditionResult) o;

        jsonObject.addProperty("id", conditionResult.getId().toString());
        jsonObject.addProperty("result", conditionResult.getResult().toString());

        return jsonObject;
    }
}
