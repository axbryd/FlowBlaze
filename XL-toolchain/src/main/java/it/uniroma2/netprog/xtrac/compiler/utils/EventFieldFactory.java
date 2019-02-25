package it.uniroma2.netprog.xtrac.compiler.utils;

import it.uniroma2.netprog.xtrac.exception.InvalidValueException;
import it.uniroma2.netprog.xtrac.exception.UnknownOpcodeException;
import it.uniroma2.netprog.xtrac.model.event.Event;
import it.uniroma2.netprog.xtrac.model.eventFieldEnums.PktRcvd;
import it.uniroma2.netprog.xtrac.model.eventFieldEnums.Timeout;
import it.uniroma2.netprog.xtrac.model.operand.EventField;

public class EventFieldFactory {
    private static EventFieldFactory ourInstance = new EventFieldFactory();

    public static EventFieldFactory getInstance() {
        return ourInstance;
    }

    private EventFieldFactory() {
    }

    public EventField getEventField(String eventName, String fieldName) throws UnknownOpcodeException, InvalidValueException {
        Event event = Event.toEvent(eventName);

        switch (event) {
            case XTRA_PKT_RCVD:
                return new EventField(PktRcvd.toEnum(fieldName).toString());

            case XTRA_TIMEOUT:
                return new EventField(Timeout.toEnum(fieldName).toString());

            default:
                throw new UnknownOpcodeException("Invalid event '"+event+
                                                 "' trying to refer to an event field ('"+event+"."+fieldName+
                                                 "')");
        }
    }
}
