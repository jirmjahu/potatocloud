package net.potatocloud.core.networking;

import lombok.RequiredArgsConstructor;
import net.potatocloud.core.networking.packet.Packet;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class Broadcast {

    private final NetworkServer server;

    private final Set<NetworkConnection> excludeConnections = new HashSet<>();
    private Predicate<NetworkConnection> filter = null;

    public Broadcast exclude(NetworkConnection connection) {
        excludeConnections.add(connection);
        return this;
    }

    public Broadcast filter(Predicate<NetworkConnection> predicate) {
        this.filter = predicate;
        return this;
    }

    public void broadcast(Packet packet) {
        server.getConnectedSessions().forEach(connection -> {
            if (excludeConnections.contains(connection)) {
                return;
            }

            if (filter != null && !filter.test(connection)) {
                return;
            }

            server.send(connection, packet);
        });
    }
}
