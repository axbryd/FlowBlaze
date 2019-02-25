package it.uniroma2.netprog.xtrac.compiler.utils;

import it.uniroma2.netprog.xtrac.exception.InvalidValueException;
import it.uniroma2.netprog.xtrac.exception.UnknownOpcodeException;
import it.uniroma2.netprog.xtrac.model.operand.PacketField;
import it.uniroma2.netprog.xtrac.model.operand.PacketFieldType;

import java.util.HashMap;

public class PacketFieldFactory {
    private static PacketFieldFactory ourInstance = new PacketFieldFactory();

    private HashMap<String, PacketField> packetFields;

    public static PacketFieldFactory getInstance() {
        return ourInstance;
    }

    private PacketFieldFactory() {
        packetFields = new HashMap<>();
        packetFields.put("packet.metadata", new PacketField("packet.metadata", "0", "4", "packet.metadata"));
    }

    public PacketField getPacketField(String fieldName) throws InvalidValueException {
        PacketField packetField = packetFields.get(fieldName);
        if(packetField == null)
            throw new InvalidValueException("The packet field '"+fieldName+"' wasn't declared");
        return packetField;
    }

    public void addPacketField(String name, PacketField packetField) {
        packetFields.put(name, packetField);
    }

    public PacketField getPacketFieldFromIndexAccess(String fieldString, String indexToken, String name) throws InvalidValueException, UnknownOpcodeException {
        PacketFieldType packetFieldType = PacketFieldType.toEnum(fieldString);

        String index = indexToken;
        index = index.substring(1, index.length()-1);
        index = index.replace(" ", "");
        String[] elements = index.split(":");

        if(elements.length == 1) {
            return new PacketField(packetFieldType.toString(), elements[0], "1", name);
        }
        else {
            String len = elements[1].contains("+") ? elements[1].substring(1) : String.valueOf(Integer.valueOf(elements[1])-Integer.valueOf(elements[0]));
            if (!elements[1].contains("+") && Integer.valueOf(elements[0]) > Integer.valueOf(elements[1]))
                throw new InvalidValueException("The beginning index must be less than equal the end ["+elements[0]+":"+elements[1]+"]");
            return new PacketField(packetFieldType.toString(), elements[0], len, name);
        }
    }
}