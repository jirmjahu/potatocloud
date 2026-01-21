package net.potatocloud.node.group.listeners;

import lombok.RequiredArgsConstructor;
import net.potatocloud.api.group.ServiceGroup;
import net.potatocloud.api.group.ServiceGroupManager;
import net.potatocloud.api.property.Property;
import net.potatocloud.core.networking.NetworkConnection;
import net.potatocloud.core.networking.packet.PacketListener;
import net.potatocloud.core.networking.packet.packets.group.GroupUpdatePacket;
import net.potatocloud.core.utils.PropertyUtil;
import net.potatocloud.node.Node;
import net.potatocloud.node.group.ServiceGroupManagerImpl;
import net.potatocloud.node.group.ServiceGroupStorage;

@RequiredArgsConstructor
public class GroupUpdateListener implements PacketListener<GroupUpdatePacket> {

    private final ServiceGroupManager groupManager;

    @Override
    public void onPacket(NetworkConnection connection, GroupUpdatePacket packet) {
        final ServiceGroup group = groupManager.getServiceGroup(packet.getGroupName());
        if (group == null) {
            return;
        }

        group.setMinOnlineCount(packet.getMinOnlineCount());
        group.setMaxOnlineCount(packet.getMaxOnlineCount());
        group.setMaxPlayers(packet.getMaxPlayers());
        group.setMaxMemory(packet.getMaxMemory());
        group.setFallback(packet.isFallback());
        group.setStartPriority(packet.getStartPriority());
        group.setStartPercentage(packet.getStartPercentage());

        group.getServiceTemplates().clear();
        packet.getServiceTemplates().forEach(group::addServiceTemplate);

        group.getCustomJvmFlags().clear();
        for (String flag : packet.getCustomJvmFlags()) {
            group.addCustomJvmFlag(flag);
        }

        group.getPropertyMap().clear();
        for (Property<?> property : packet.getPropertyMap().values()) {
            PropertyUtil.setPropertyUnchecked(group, property);
        }

        if (groupManager instanceof ServiceGroupManagerImpl groupManager) {
            ServiceGroupStorage.saveToFile(group, groupManager.getGroupsPath());
        }

        Node.getInstance().getServer().getConnectedSessions().stream()
                .filter(networkConnection -> !networkConnection.equals(connection))
                .forEach(networkConnection -> networkConnection.send(packet));
    }
}
