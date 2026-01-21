package net.potatocloud.core.networking.packet.packets.platform;

import net.potatocloud.core.networking.packet.Packet;
import net.potatocloud.core.networking.packet.PacketIds;
import net.potatocloud.core.networking.netty.PacketBuffer;

public class RequestPlatformsPacket implements Packet {

    @Override
    public int getId() {
        return PacketIds.REQUEST_PLATFORMS;
    }

    @Override
    public void write(PacketBuffer buf) {

    }

    @Override
    public void read(PacketBuffer buf) {

    }
}
