package net.potatocloud.core.networking;

import net.potatocloud.core.networking.packet.Packet;

import java.util.List;

public interface NetworkServer extends NetworkComponent {

    void start(String hostname, int port);

    boolean isRunning();

    List<NetworkConnection> getConnectedSessions();

    int getPort();

    void send(NetworkConnection client, Packet packet);

    default Broadcast generateBroadcast() {
        return new Broadcast(this);
    }
}
