package net.potatocloud.node.player.listeners;

import lombok.RequiredArgsConstructor;
import net.potatocloud.api.player.CloudPlayer;
import net.potatocloud.api.player.impl.CloudPlayerImpl;
import net.potatocloud.core.networking.NetworkConnection;
import net.potatocloud.core.networking.packet.PacketListener;
import net.potatocloud.core.networking.packet.packets.player.CloudPlayerAddPacket;
import net.potatocloud.node.Node;
import net.potatocloud.node.player.CloudPlayerManagerImpl;

@RequiredArgsConstructor
public class CloudPlayerAddListener implements PacketListener<CloudPlayerAddPacket> {

    private final CloudPlayerManagerImpl playerManager;

    @Override
    public void onPacket(NetworkConnection connection, CloudPlayerAddPacket packet) {
        final CloudPlayer player = new CloudPlayerImpl(packet.getUsername(), packet.getUniqueId(), packet.getConnectedProxyName());

        playerManager.registerPlayer(player);

        final Node node = Node.getInstance();

        node.getServer().getConnectedSessions().stream()
                .filter(networkConnection -> !networkConnection.equals(connection))
                .forEach(networkConnection -> networkConnection.send(packet));

        if (node.getConfig().isLogPlayerConnections()) {
            node.getLogger().info("Player &a" + player.getUsername() + " &7connected to the network &8[&7UUID&8: &a"
                    + player.getUniqueId() + "&8, &7Proxy&8: &a" + player.getConnectedProxyName() + "&8]");
        }
    }
}
