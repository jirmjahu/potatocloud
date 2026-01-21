package net.potatocloud.core.networking.packet;

import net.potatocloud.core.networking.NetworkConnection;

public interface PacketListener<T extends Packet> {

    void onPacket(NetworkConnection connection, T packet);

}
