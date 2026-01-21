package net.potatocloud.core.networking.packet.packets.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.potatocloud.core.networking.packet.Packet;
import net.potatocloud.core.networking.packet.PacketIds;
import net.potatocloud.core.networking.netty.PacketBuffer;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventPacket implements Packet {

    private String eventClass;
    private String json;

    @Override
    public int getId() {
        return PacketIds.EVENT;
    }

    @Override
    public void write(PacketBuffer buf) {
        buf.writeString(eventClass);
        buf.writeString(json);
    }

    @Override
    public void read(PacketBuffer buf) {
        eventClass = buf.readString();
        json = buf.readString();
    }
}
