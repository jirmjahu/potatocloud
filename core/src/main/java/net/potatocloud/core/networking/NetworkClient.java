package net.potatocloud.core.networking;

public interface NetworkClient {

    void connect(String host, int port);

    void send(Packet packet);

    void disconnect();

    <T extends Packet> void registerPacketListener(int id, PacketListener<T> listener);

    boolean isConnected();

    void addConnectionListener(ConnectionListener listener);

}
