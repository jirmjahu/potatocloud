package net.potatocloud.core.networking.packet.packets.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.potatocloud.core.networking.packet.Packet;
import net.potatocloud.core.networking.packet.PacketIds;
import net.potatocloud.core.networking.netty.PacketBuffer;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StopServicePacket implements Packet {

    private String serviceName;

    @Override
    public int getId() {
        return PacketIds.STOP_SERVICE;
    }

    @Override
    public void write(PacketBuffer buf) {
        buf.writeString(serviceName);
    }

    @Override
    public void read(PacketBuffer buf) {
        serviceName = buf.readString();
    }
}
