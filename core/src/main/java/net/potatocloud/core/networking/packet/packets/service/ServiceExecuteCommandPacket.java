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
public class ServiceExecuteCommandPacket implements Packet {

    private String serviceName;
    private String command;

    @Override
    public int getId() {
        return PacketIds.SERVICE_EXECUTE_COMMAND;
    }

    @Override
    public void write(PacketBuffer buf) {
        buf.writeString(serviceName);
        buf.writeString(command);
    }

    @Override
    public void read(PacketBuffer buf) {
        serviceName = buf.readString();
        command = buf.readString();
    }
}
