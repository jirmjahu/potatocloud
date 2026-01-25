package net.potatocloud.core.networking.packet.packets.group;

import lombok.NoArgsConstructor;
import net.potatocloud.core.networking.netty.PacketBuffer;
import net.potatocloud.core.networking.packet.Packet;
import net.potatocloud.core.networking.packet.PacketIds;

@NoArgsConstructor
public class RequestGroupsPacket implements Packet {

    @Override
    public int getId() {
        return PacketIds.REQUEST_GROUPS;
    }

    @Override
    public void write(PacketBuffer buf) {
    }

    @Override
    public void read(PacketBuffer buf) {
    }

}
