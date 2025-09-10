package net.potatocloud.plugin.impl;

import lombok.Getter;
import net.potatocloud.api.CloudAPI;
import net.potatocloud.api.event.EventManager;
import net.potatocloud.api.group.ServiceGroupManager;
import net.potatocloud.api.platform.PlatformManager;
import net.potatocloud.api.player.CloudPlayerManager;
import net.potatocloud.api.service.ServiceManager;
import net.potatocloud.core.event.ClientEventManager;
import net.potatocloud.core.networking.NetworkClient;
import net.potatocloud.core.networking.PacketManager;
import net.potatocloud.core.networking.netty.NettyNetworkClient;
import net.potatocloud.plugin.impl.group.ServiceGroupManagerImpl;
import net.potatocloud.plugin.impl.platform.PlatformManagerImpl;
import net.potatocloud.plugin.impl.player.CloudPlayerManagerImpl;
import net.potatocloud.plugin.impl.service.ServiceManagerImpl;

@Getter
public class PluginCloudAPI extends CloudAPI {

    private final PacketManager packetManager;
    private final NetworkClient client;
    private final ServiceGroupManager groupManager;
    private final ServiceManager serviceManager;
    private final PlatformManager platformManager;
    private final CloudPlayerManager playerManager;
    private final ClientEventManager eventManager;

    public PluginCloudAPI() {
        packetManager = new PacketManager();
        client = new NettyNetworkClient(packetManager);
        client.connect("127.0.0.1", Integer.parseInt(System.getProperty("potatocloud.node.port")));
        groupManager = new ServiceGroupManagerImpl(client);
        serviceManager = new ServiceManagerImpl(client);
        platformManager = new PlatformManagerImpl(client);
        playerManager = new CloudPlayerManagerImpl(client);
        eventManager = new ClientEventManager(client);
    }

    public static PluginCloudAPI getInstance() {
        return (PluginCloudAPI) CloudAPI.getInstance();
    }

    public void shutdown() {
        client.disconnect();
    }

    @Override
    public ServiceGroupManager getServiceGroupManager() {
        return groupManager;
    }

    @Override
    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    @Override
    public PlatformManager getPlatformManager() {
        return platformManager;
    }

    @Override
    public EventManager getEventManager() {
        return eventManager;
    }

    @Override
    public CloudPlayerManager getPlayerManager() {
        return playerManager;
    }
}
