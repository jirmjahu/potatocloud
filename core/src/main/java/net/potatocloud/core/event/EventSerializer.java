package net.potatocloud.core.event;

import com.google.gson.Gson;
import net.potatocloud.api.event.Event;
import net.potatocloud.core.networking.packet.packets.event.EventPacket;

public class EventSerializer {

    private static final Gson gson = new Gson();

    public static EventPacket serialize(Event event) {
        return new EventPacket(event.getClass().getName(), gson.toJson(event));
    }

    public static Event deserialize(EventPacket packet) {
        try {
            final Class<?> clazz = Class.forName(packet.getEventClass());
            return (Event) gson.fromJson(packet.getJson(), clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
