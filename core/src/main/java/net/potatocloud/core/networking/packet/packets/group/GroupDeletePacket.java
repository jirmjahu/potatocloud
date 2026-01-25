package net.potatocloud.core.networking.packet.packets.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.potatocloud.core.networking.netty.PacketBuffer;
import net.potatocloud.core.networking.packet.Packet;
import net.potatocloud.core.networking.packet.PacketIds;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDeletePacket implements Packet {

    private String groupName;

    @Override
    public int getId() {
        return PacketIds.GROUP_DELETE;
    }

    @Override
    public void write(PacketBuffer buf) {
        buf.writeString(groupName);
    }

    @Override
    public void read(PacketBuffer buf) {
        groupName = buf.readString();
    }
}
