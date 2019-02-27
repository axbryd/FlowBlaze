package it.uniroma2.netprog.xtrac.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.uniroma2.netprog.xtrac.model.operand.*;
import java.lang.reflect.Type;

public class OperandSerializer implements JsonSerializer {
    @Override
    public JsonElement serialize(Object o, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("type", OperandType.toEnum((Operand) o).toString());

        if(o.getClass() == LocalRegister.class || o.getClass() == GlobalRegister.class || o.getClass() == ReadOnlyRegister.class)
            jsonObject.addProperty("name", o.toString());
        else if (o.getClass() == PacketField.class) {
            PacketField packetField = (PacketField) o;
            jsonObject.addProperty("name", packetField.getName());
            jsonObject.addProperty("value", packetField.getValue());
        }
        else
            jsonObject.addProperty("value", o.toString());

        if(o.getClass() == PacketField.class){
            PacketField packetField = (PacketField) o;
            jsonObject.addProperty("from", ((PacketField) o).getFrom());
            jsonObject.addProperty("length", ((PacketField) o).getLength());
        }

        return jsonObject;
    }
}
