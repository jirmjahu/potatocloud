package net.potatocloud.core.networking.packet.packets.platform;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.potatocloud.core.networking.netty.PacketBuffer;
import net.potatocloud.core.networking.packet.Packet;
import net.potatocloud.core.networking.packet.PacketIds;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlatformRemovePacket implements Packet {

    private String platformName;

    @Override
    public int getId() {
        return PacketIds.PLATFORM_REMOVE;
    }

    @Override
    public void write(PacketBuffer buf) {
        buf.writeString(platformName);
    }

    @Override
    public void read(PacketBuffer buf) {
        platformName = buf.readString();
    }
}
