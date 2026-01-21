package net.potatocloud.core.networking.packet.packets.platform;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.potatocloud.api.platform.Platform;
import net.potatocloud.core.networking.netty.PacketBuffer;
import net.potatocloud.core.networking.packet.Packet;
import net.potatocloud.core.networking.packet.PacketIds;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlatformUpdatePacket implements Packet {

    private Platform platform;

    @Override
    public int getId() {
        return PacketIds.PLATFORM_UPDATE;
    }

    @Override
    public void write(PacketBuffer buf) {
        buf.writePlatform(platform);
    }

    @Override
    public void read(PacketBuffer buf) {
        platform = buf.readPlatform();
    }
}
