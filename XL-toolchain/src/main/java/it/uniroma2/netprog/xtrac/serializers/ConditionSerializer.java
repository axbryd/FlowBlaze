package it.uniroma2.netprog.xtrac.serializers;

import com.google.gson.*;
import it.uniroma2.netprog.xtrac.model.condition.Condition;
import it.uniroma2.netprog.xtrac.model.operand.*;
import java.lang.reflect.Type;

public class ConditionSerializer implements JsonSerializer{
    @Override
    public JsonElement serialize(Object o, Type type, JsonSerializationContext jsonSerializationContext) {
        final JsonObject jsonObject = new JsonObject();
        Condition condition = (Condition) o;

        Gson standardGson = new Gson();

        final GsonBuilder operandBuilder = new GsonBuilder();
        operandBuilder.registerTypeAdapter(GlobalRegister.class, new OperandSerializer());
        operandBuilder.registerTypeAdapter(LocalRegister.class, new OperandSerializer());
        operandBuilder.registerTypeAdapter(IPv4.class, new OperandSerializer());
        operandBuilder.registerTypeAdapter(IPv6.class, new OperandSerializer());
        operandBuilder.registerTypeAdapter(MAC.class, new OperandSerializer());
        operandBuilder.registerTypeAdapter(PacketField.class, new OperandSerializer());
        operandBuilder.registerTypeAdapter(IntegerConstant.class, new OperandSerializer());
        operandBuilder.registerTypeAdapter(EventField.class, new OperandSerializer());
        operandBuilder.registerTypeAdapter(ReadOnlyRegister.class, new OperandSerializer());
        final Gson operandGson = operandBuilder.create();

        jsonObject.addProperty("id", condition.toString());

        jsonObject.addProperty("opcode", condition.getOpcode().toString());

        JsonObject op = standardGson.fromJson(operandGson.toJson(condition.getOp0()), JsonObject.class);
        jsonObject.add("op0", op);

        op = standardGson.fromJson(operandGson.toJson(condition.getOp1()), JsonObject.class);
        jsonObject.add("op1", op);

        return jsonObject;
    }
}
