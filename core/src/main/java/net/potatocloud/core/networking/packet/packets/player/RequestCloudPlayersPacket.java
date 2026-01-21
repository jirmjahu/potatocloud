package net.potatocloud.core.networking.packet.packets.player;

import lombok.NoArgsConstructor;
import net.potatocloud.core.networking.packet.Packet;
import net.potatocloud.core.networking.packet.PacketIds;
import net.potatocloud.core.networking.netty.PacketBuffer;

@NoArgsConstructor
public class RequestCloudPlayersPacket implements Packet {

    @Override
    public int getId() {
        return PacketIds.REQUEST_PLAYERS;
    }

    @Override
    public void write(PacketBuffer buf) {

    }

    @Override
    public void read(PacketBuffer buf) {

    }
}
