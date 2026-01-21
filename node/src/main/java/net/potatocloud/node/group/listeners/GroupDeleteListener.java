package net.potatocloud.node.group.listeners;

import lombok.RequiredArgsConstructor;
import net.potatocloud.api.group.ServiceGroup;
import net.potatocloud.api.group.ServiceGroupManager;
import net.potatocloud.core.networking.NetworkConnection;
import net.potatocloud.core.networking.NetworkServer;
import net.potatocloud.core.networking.packet.PacketListener;
import net.potatocloud.core.networking.packet.packets.group.GroupDeletePacket;

@RequiredArgsConstructor
public class GroupDeleteListener implements PacketListener<GroupDeletePacket> {

    private final ServiceGroupManager groupManager;
    private final NetworkServer server;

    @Override
    public void onPacket(NetworkConnection connection, GroupDeletePacket packet) {
        final ServiceGroup group = groupManager.getServiceGroup(packet.getGroupName());
        if (group == null) {
            return;
        }

        groupManager.deleteServiceGroup(group);

        server.generateBroadcast().exclude(connection).broadcast(packet);
    }
}
