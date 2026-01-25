package net.potatocloud.node.player.listeners;

import lombok.RequiredArgsConstructor;
import net.potatocloud.api.player.CloudPlayer;
import net.potatocloud.api.player.impl.CloudPlayerImpl;
import net.potatocloud.core.networking.NetworkConnection;
import net.potatocloud.core.networking.NetworkServer;
import net.potatocloud.core.networking.packet.PacketListener;
import net.potatocloud.core.networking.packet.packets.player.CloudPlayerAddPacket;
import net.potatocloud.node.Node;
import net.potatocloud.node.config.NodeConfig;
import net.potatocloud.node.player.CloudPlayerManagerImpl;

@RequiredArgsConstructor
public class CloudPlayerAddListener implements PacketListener<CloudPlayerAddPacket> {

    private final CloudPlayerManagerImpl playerManager;
    private final NetworkServer server;

    @Override
    public void onPacket(NetworkConnection connection, CloudPlayerAddPacket packet) {
        final CloudPlayer player = new CloudPlayerImpl(packet.getUsername(), packet.getUniqueId(), packet.getConnectedProxyName());

        playerManager.registerPlayer(player);

        final Node node = Node.getInstance();

        server.generateBroadcast().exclude(connection).broadcast(packet);

        final NodeConfig config = node.getConfig();
        if (config.isLogPlayerConnections()) {
            node.getLogger().info("Player &a" + player.getUsername() + " &7connected to the network &8[&7UUID&8: &a"
                    + player.getUniqueId() + "&8, &7Proxy&8: &a" + player.getConnectedProxyName() + "&8]");
        }
    }
}
