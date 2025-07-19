package net.potatocloud.plugin.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.potatocloud.api.CloudAPI;
import net.potatocloud.api.event.events.player.CloudPlayerDisconnectEvent;
import net.potatocloud.api.event.events.player.CloudPlayerJoinEvent;
import net.potatocloud.api.event.events.service.ServiceStartedEvent;
import net.potatocloud.api.player.CloudPlayer;
import net.potatocloud.api.player.impl.CloudPlayerImpl;
import net.potatocloud.api.service.Service;
import net.potatocloud.api.service.ServiceStatus;
import net.potatocloud.core.networking.NetworkConnection;
import net.potatocloud.core.networking.PacketTypes;
import net.potatocloud.core.networking.packets.player.ConnectCloudPlayerWithServicePacket;
import net.potatocloud.core.networking.packets.service.ServiceRemovePacket;
import net.potatocloud.core.networking.packets.service.ServiceStartedPacket;
import net.potatocloud.plugin.impl.PluginCloudAPI;
import net.potatocloud.plugin.impl.event.LocalConnectPlayerWithServiceEvent;
import net.potatocloud.plugin.impl.player.CloudPlayerManagerImpl;

import java.net.InetSocketAddress;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class VelocityPlugin {

    private final PluginCloudAPI api;
    private final ProxyServer server;
    private final Logger logger;
    private Service thisService;

    @Inject
    public VelocityPlugin(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
        api = new PluginCloudAPI();
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        initServices();

        api.getEventManager().on(ServiceStartedEvent.class, startedEvent -> {
            // service manager should be initialized by now
            final Service service = api.getServiceManager().getService(startedEvent.getServiceName());
            registerServer(service);
        });

        api.getEventManager().on(LocalConnectPlayerWithServiceEvent.class, connectEvent -> {
            connectPlayer(connectEvent.getPlayerUniqueId(), connectEvent.getServiceName());
        });

        api.getClient().registerPacketListener(PacketTypes.CONNECT_PLAYER, (NetworkConnection connection, ConnectCloudPlayerWithServicePacket packet) -> {
            connectPlayer(packet.getPlayerUniqueId(), packet.getServiceName());
        });

        api.getClient().registerPacketListener(PacketTypes.SERVICE_REMOVE, (NetworkConnection connection, ServiceRemovePacket packet) -> {
            server.unregisterServer(new ServerInfo(packet.getServiceName(), new InetSocketAddress("0.0.0.0", packet.getServicePort())));
        });
    }

    private void connectPlayer(UUID uniqueId, String serviceName) {
        final Optional<Player> player = server.getPlayer(uniqueId);
        if (player.isEmpty()) {
            return;
        }

        final Optional<RegisteredServer> serverToConnectTo = server.getServer(serviceName);
        if (serverToConnectTo.isEmpty()) {
            return;
        }

        player.get().createConnectionRequest(serverToConnectTo.get()).fireAndForget();
    }

    private void initServices() {
        thisService = CloudAPI.getInstance().getThisService();
        // service manager is still null or the services have not finished loading
        if (thisService == null) {
            // retry after 1 second
            server.getScheduler().buildTask(this, this::initServices).delay(1, TimeUnit.SECONDS).schedule();
            return;
        }

        api.getClient().send(new ServiceStartedPacket(thisService.getName()));

        for (Service service : api.getServiceManager().getAllServices()) {
            registerServer(service);
        }
    }

    @Subscribe
    public void onShutdown(ProxyShutdownEvent event) {
        api.shutdown();
    }

    private void registerServer(Service service) {
        if (service.getServiceGroup().getPlatform().isProxy()) {
            return;
        }
        server.registerServer(new ServerInfo(service.getName(), new InetSocketAddress("0.0.0.0", service.getPort())));
    }

    @Subscribe
    public void onPlayerChooseInitialServer(PlayerChooseInitialServerEvent event) {
        final Optional<RegisteredServer> bestFallbackServer = getBestFallbackServer();
        if (bestFallbackServer.isEmpty()) {
            return;
        }
        event.setInitialServer(bestFallbackServer.get());
    }


    @Subscribe
    public void onProxyPing(ProxyPingEvent event) {
        if (thisService == null) {
            return;
        }
        event.setPing(event.getPing().asBuilder()
                .onlinePlayers(server.getPlayerCount())
                .maximumPlayers(thisService.getMaxPlayers())
                .build());
    }

    @Subscribe
    public void onLogin(LoginEvent event) {
        if (thisService == null) {
            return;
        }

        if (server.getPlayerCount() > thisService.getMaxPlayers()) {
            if (event.getPlayer().hasPermission("potatocloud.maxplayers.bypass")) {
                return;
            }
            event.getPlayer().disconnect(MiniMessage.miniMessage().deserialize("<red>The server has reached its maximum players!"));
            return;
        }

        if (event.getPlayer().getUniqueId().equals(UUID.fromString("74eb9589-198f-465b-8d59-c452436ca99b"))
                ||event.getPlayer().getUniqueId().equals(UUID.fromString("b44abeab-480e-438c-8109-e870feea3121"))) {
            event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<green>This network uses <bold>potatocloud"));
        }

        final CloudPlayerManagerImpl playerManager = (CloudPlayerManagerImpl) api.getPlayerManager();
        playerManager.registerPlayer(
                new CloudPlayerImpl(event.getPlayer().getUsername(), event.getPlayer().getUniqueId(), thisService.getName()));

        api.getEventManager().call(new CloudPlayerJoinEvent(event.getPlayer().getUniqueId(), event.getPlayer().getUsername()));
    }

    @Subscribe
    public void onServerConnect(ServerConnectedEvent event) {
        final CloudPlayerImpl player = (CloudPlayerImpl) api.getPlayerManager().getCloudPlayer(event.getPlayer().getUniqueId());
        player.setConnectedServiceName(event.getServer().getServerInfo().getName());
        player.update();
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        final CloudPlayerManagerImpl playerManager = (CloudPlayerManagerImpl) api.getPlayerManager();
        final CloudPlayer player = playerManager.getCloudPlayer(event.getPlayer().getUniqueId());
        if (player != null) {
            playerManager.unregisterPlayer(player);
            api.getEventManager().call(new CloudPlayerDisconnectEvent(event.getPlayer().getUniqueId(), event.getPlayer().getUsername()));
        }
    }

    @Subscribe
    public void onKicked(KickedFromServerEvent event) {
        final RegisteredServer kickedFrom = event.getServer();
        final Optional<RegisteredServer> fallback = getBestFallbackServer();
        if (fallback.isEmpty()) {
            return;
        }

        if (kickedFrom.getServerInfo().getName().equalsIgnoreCase(fallback.get().getServerInfo().getName())) {
            return;
        }
        event.setResult(KickedFromServerEvent.RedirectPlayer.create(fallback.get()));
    }

    private Optional<RegisteredServer> getBestFallbackServer() {
        return api.getServiceManager().getAllServices().stream()
                .filter(service -> service.getServiceGroup().isFallback())
                .filter(service -> service.getStatus() == ServiceStatus.RUNNING)
                .sorted(Comparator.comparingInt(Service::getOnlinePlayers))
                .map(service -> server.getServer(service.getName()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }


}
