package net.potatocloud.core.networking.packet.packets.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.potatocloud.api.property.Property;
import net.potatocloud.core.networking.netty.PacketBuffer;
import net.potatocloud.core.networking.packet.Packet;
import net.potatocloud.core.networking.packet.PacketIds;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyAddPacket implements Packet {

    private Property<?> property;

    @Override
    public void write(PacketBuffer buf) {
        buf.writeProperty(property);
    }

    @Override
    public void read(PacketBuffer buf) {
        property = buf.readProperty();
    }

    @Override
    public int getId() {
        return PacketIds.PROPERTY_ADD;
    }
}
