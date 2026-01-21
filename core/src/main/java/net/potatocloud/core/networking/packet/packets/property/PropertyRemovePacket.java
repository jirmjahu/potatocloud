package net.potatocloud.core.networking.packet.packets.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.potatocloud.core.networking.packet.Packet;
import net.potatocloud.core.networking.packet.PacketIds;
import net.potatocloud.core.networking.netty.PacketBuffer;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyRemovePacket implements Packet {

    private String name;

    @Override
    public int getId() {
        return PacketIds.PROPERTY_REMOVE;
    }

    @Override
    public void write(PacketBuffer buf) {
        buf.writeString(name);
    }

    @Override
    public void read(PacketBuffer buf) {
        name = buf.readString();
    }
}
