package it.uniroma2.netprog.xtrac.serializers;

import com.google.gson.*;
import it.uniroma2.netprog.xtrac.model.action.*;
import it.uniroma2.netprog.xtrac.model.operand.*;
import java.lang.reflect.Type;

public class ActionSerializer implements JsonSerializer {
    @Override
    public JsonElement serialize(Object o, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();

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

        if (o.getClass() == Update.class) {
            Update update = (Update) o;
            jsonObject.addProperty("opcode", update.getOpcode().toString());

            JsonObject op = standardGson.fromJson(operandGson.toJson(update.getOp0()), JsonObject.class);
            jsonObject.add("op0", op);

            op = standardGson.fromJson(operandGson.toJson(update.getOp1()), JsonObject.class);
            jsonObject.add("op1", op);

            op = standardGson.fromJson(operandGson.toJson(update.getOp2()), JsonObject.class);
            jsonObject.add("op2", op);
            // useless because op2 must be a register!
        }
        else if (o.getClass() == HardwareAction.class) {
            HardwareAction hardwareAction = (HardwareAction) o;

            jsonObject.addProperty("opcode", hardwareAction.getOpcode().toString());


            JsonObject op;
            if(hardwareAction.getOp0() != null) {
                op = standardGson.fromJson(operandGson.toJson(hardwareAction.getOp0()), JsonObject.class);
                jsonObject.add("op0", op);
            }

            if(hardwareAction.getOp1() != null) {
                op = standardGson.fromJson(operandGson.toJson(hardwareAction.getOp1()), JsonObject.class);
                jsonObject.add("op1", op);
            }
            if(hardwareAction.getOp2() != null) {
                op = standardGson.fromJson(operandGson.toJson(hardwareAction.getOp2()), JsonObject.class);
                jsonObject.add("op2", op);
            }
        }
        else {
            SequentialActions serialActions = (SequentialActions) o;

            JsonObject serialActionsJson = new JsonObject();
        }

        return jsonObject;
    }
}