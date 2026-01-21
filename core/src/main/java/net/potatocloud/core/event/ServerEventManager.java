package net.potatocloud.core.event;

import net.potatocloud.api.event.Event;
import net.potatocloud.core.networking.NetworkServer;
import net.potatocloud.core.networking.packet.packets.event.EventPacket;

public class ServerEventManager extends BaseEventManager {

    private final NetworkServer server;

    public ServerEventManager(NetworkServer server) {
        this.server = server;

        server.on(EventPacket.class, (connection, packet) -> {
            final Event event = EventSerializer.deserialize(packet);
            if (event != null) {
                callLocal(event);

                server.getConnectedSessions().stream()
                        .filter(networkConnection -> !networkConnection.equals(connection))
                        .forEach(networkConnection -> networkConnection.send(packet));
            }
        });
    }

    @Override
    public <T extends Event> void call(T event) {
        callLocal(event);
        server.generateBroadcast().broadcast(EventSerializer.serialize(event));
    }
}
