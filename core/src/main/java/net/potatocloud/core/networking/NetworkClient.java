package net.potatocloud.core.networking;

import net.potatocloud.core.networking.packet.Packet;

public interface NetworkClient extends NetworkComponent {

    void connect(String host, int port);

    void send(Packet packet);

    void close();

    boolean isConnected();

    void addConnectionListener(ConnectionListener listener);

}
