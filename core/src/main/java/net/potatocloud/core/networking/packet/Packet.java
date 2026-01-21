package net.potatocloud.core.networking.packet;

import net.potatocloud.core.networking.netty.PacketBuffer;

public interface Packet {

    int getId();

    void write(PacketBuffer buf);

    void read(PacketBuffer buf);

}
