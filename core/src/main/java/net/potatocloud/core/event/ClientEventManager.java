package net.potatocloud.core.event;

import net.potatocloud.api.event.Event;
import net.potatocloud.core.networking.NetworkClient;
import net.potatocloud.core.networking.packet.packets.event.EventPacket;

public class ClientEventManager extends BaseEventManager {

    private final NetworkClient client;

    public ClientEventManager(NetworkClient client) {
        this.client = client;

        client.on(EventPacket.class, (connection, packet) -> {
            final Event event = EventSerializer.deserialize(packet);
            if (event != null) {
                callLocal(event);
            }
        });
    }

    @Override
    public <T extends Event> void call(T event) {
        callLocal(event);
        client.send(EventSerializer.serialize(event));
    }
}
