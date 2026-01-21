package net.potatocloud.core.networking.packet.packets.service;

import lombok.NoArgsConstructor;
import net.potatocloud.core.networking.packet.Packet;
import net.potatocloud.core.networking.packet.PacketIds;
import net.potatocloud.core.networking.netty.PacketBuffer;

@NoArgsConstructor
public class RequestServicesPacket implements Packet {

    @Override
    public int getId() {
        return PacketIds.REQUEST_SERVICES;
    }

    @Override
    public void write(PacketBuffer buf) {

    }

    @Override
    public void read(PacketBuffer buf) {

    }
}
