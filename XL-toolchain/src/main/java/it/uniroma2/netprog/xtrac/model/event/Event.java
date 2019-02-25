package it.uniroma2.netprog.xtrac.model.event;

import it.uniroma2.netprog.xtrac.exception.UnknownOpcodeException;

import java.util.Vector;

public enum Event {
    XTRA_PKT_RCVD,
    XTRA_TIMEOUT;

    public static Event toEvent(String name) throws UnknownOpcodeException {
        switch (name)
        {
            case "pktRcvd":
                return XTRA_PKT_RCVD;

            case "timeout":
                return XTRA_TIMEOUT;

            default:
                throw new UnknownOpcodeException("Unknown event '"+name+"'");
        }
    }

    public static String getNameFromEnum(Event event) {
        switch (event) {
            case XTRA_TIMEOUT:
                return "timeout";
            case XTRA_PKT_RCVD:
                return "pktRcvd";
            default:
                return null;
        }
    }

    public static Vector<Event> getEvents()
    {
        Vector<Event> events = new Vector<>();
        events.add(XTRA_PKT_RCVD);
        events.add(XTRA_TIMEOUT);

        return events;
    }
}
