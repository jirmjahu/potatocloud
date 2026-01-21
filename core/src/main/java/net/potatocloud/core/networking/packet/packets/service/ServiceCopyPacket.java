package net.potatocloud.core.networking.packet.packets.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.potatocloud.core.networking.netty.PacketBuffer;
import net.potatocloud.core.networking.packet.Packet;
import net.potatocloud.core.networking.packet.PacketIds;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCopyPacket implements Packet {

    private String serviceName;
    private String templateName;
    private String filter;

    @Override
    public int getId() {
        return PacketIds.SERVICE_COPY;
    }

    @Override
    public void write(PacketBuffer buf) {
        buf.writeString(serviceName);
        buf.writeString(templateName);
        buf.writeString(filter);
    }

    @Override
    public void read(PacketBuffer buf) {
        serviceName = buf.readString();
        templateName = buf.readString();
        filter = buf.readString();
    }
}
