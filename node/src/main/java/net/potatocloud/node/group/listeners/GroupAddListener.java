package net.potatocloud.node.group.listeners;

import lombok.RequiredArgsConstructor;
import net.potatocloud.api.group.ServiceGroupManager;
import net.potatocloud.core.networking.NetworkConnection;
import net.potatocloud.core.networking.PacketListener;
import net.potatocloud.core.networking.packets.group.GroupAddPacket;
import net.potatocloud.node.Node;

@RequiredArgsConstructor
public class GroupAddListener implements PacketListener<GroupAddPacket> {

    private final ServiceGroupManager groupManager;

    @Override
    public void onPacket(NetworkConnection connection, GroupAddPacket packet) {
        groupManager.createServiceGroup(
                packet.getName(),
                packet.getPlatformName(),
                packet.getPlatformVersionName(),
                packet.getMinOnlineCount(),
                packet.getMaxOnlineCount(),
                packet.getMaxPlayers(),
                packet.getMaxMemory(),
                packet.isFallback(),
                packet.isStatic(),
                packet.getStartPriority(),
                packet.getStartPercentage(),
                packet.getJavaCommand(),
                packet.getCustomJvmFlags(),
                packet.getProperties()
        );

        Node.getInstance().getServer().getConnectedSessions().stream()
                .filter(networkConnection -> !networkConnection.equals(connection))
                .forEach(networkConnection -> networkConnection.send(packet));
    }
}
