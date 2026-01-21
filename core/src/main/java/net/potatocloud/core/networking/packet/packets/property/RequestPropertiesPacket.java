package net.potatocloud.core.networking.packet.packets.property;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.potatocloud.core.networking.netty.PacketBuffer;
import net.potatocloud.core.networking.packet.Packet;
import net.potatocloud.core.networking.packet.PacketIds;

@Data
@NoArgsConstructor
public class RequestPropertiesPacket implements Packet {

    @Override
    public int getId() {
        return PacketIds.REQUEST_PROPERTIES;
    }

    @Override
    public void write(PacketBuffer buf) {

    }

    @Override
    public void read(PacketBuffer buf) {

    }
}
