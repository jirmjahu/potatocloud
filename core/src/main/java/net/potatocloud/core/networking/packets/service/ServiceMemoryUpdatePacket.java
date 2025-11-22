package net.potatocloud.core.networking.packets.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.potatocloud.core.networking.Packet;
import net.potatocloud.core.networking.PacketIds;
import net.potatocloud.core.networking.netty.PacketBuffer;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceMemoryUpdatePacket implements Packet {

    private String serviceName;
    private int usedMemory;

    @Override
    public int getId() {
        return PacketIds.SERVICE_MEMORY_UPDATE;
    }

    @Override
    public void write(PacketBuffer buf) {
        buf.writeString(serviceName);
        buf.writeInt(usedMemory);
    }

    @Override
    public void read(PacketBuffer buf) {
        serviceName = buf.readString();
        usedMemory = buf.readInt();
    }
}
