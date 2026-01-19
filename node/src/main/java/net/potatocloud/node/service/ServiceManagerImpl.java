package net.potatocloud.node.service;

import net.potatocloud.api.event.EventManager;
import net.potatocloud.api.group.ServiceGroup;
import net.potatocloud.api.group.ServiceGroupManager;
import net.potatocloud.api.service.Service;
import net.potatocloud.api.service.ServiceManager;
import net.potatocloud.core.networking.NetworkServer;
import net.potatocloud.core.networking.PacketIds;
import net.potatocloud.core.networking.packets.service.ServiceAddPacket;
import net.potatocloud.core.networking.packets.service.ServiceUpdatePacket;
import net.potatocloud.node.config.NodeConfig;
import net.potatocloud.node.console.Console;
import net.potatocloud.node.console.Logger;
import net.potatocloud.node.platform.DownloadManager;
import net.potatocloud.node.platform.PlatformManagerImpl;
import net.potatocloud.node.platform.cache.CacheManager;
import net.potatocloud.node.screen.ScreenManager;
import net.potatocloud.node.service.listeners.*;
import net.potatocloud.node.template.TemplateManager;
import net.potatocloud.node.utils.NetworkUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class ServiceManagerImpl implements ServiceManager {

    private final List<Service> services = new CopyOnWriteArrayList<>();

    private final NodeConfig config;
    private final Logger logger;
    private final NetworkServer server;
    private final EventManager eventManager;
    private final ServiceGroupManager groupManager;
    private final ScreenManager screenManager;
    private final TemplateManager templateManager;
    private final PlatformManagerImpl platformManager;
    private final DownloadManager downloadManager;
    private final CacheManager cacheManager;
    private final Console console;

    public ServiceManagerImpl(
            NodeConfig config,
            Logger logger,
            NetworkServer server,
            EventManager eventManager,
            ServiceGroupManager groupManager,
            ScreenManager screenManager,
            TemplateManager templateManager,
            PlatformManagerImpl platformManager,
            DownloadManager downloadManager,
            CacheManager cacheManager,
            Console console
    ) {
        this.config = config;
        this.logger = logger;
        this.server = server;
        this.eventManager = eventManager;
        this.groupManager = groupManager;
        this.screenManager = screenManager;
        this.templateManager = templateManager;
        this.platformManager = platformManager;
        this.downloadManager = downloadManager;
        this.cacheManager = cacheManager;
        this.console = console;

        server.registerPacketListener(PacketIds.REQUEST_SERVICES, new RequestServicesListener(this));
        server.registerPacketListener(PacketIds.SERVICE_STARTED, new ServiceStartedListener(this, logger, eventManager));
        server.registerPacketListener(PacketIds.SERVICE_UPDATE, new ServiceUpdateListener(this));
        server.registerPacketListener(PacketIds.START_SERVICE, new StartServiceListener(this, groupManager));
        server.registerPacketListener(PacketIds.STOP_SERVICE, new StopServiceListener(this));
        server.registerPacketListener(PacketIds.SERVICE_EXECUTE_COMMAND, new ServiceExecuteCommandListener(this));
        server.registerPacketListener(PacketIds.SERVICE_COPY, new ServiceCopyListener(this));
    }

    @Override
    public Service getService(String name) {
        return services.stream()
                .filter(service -> service.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Service> getAllServices() {
        return Collections.unmodifiableList(services);
    }

    @Override
    public void updateService(Service service) {
        server.broadcastPacket(new ServiceUpdatePacket(
                service.getName(),
                service.getStatus().name(),
                service.getMaxPlayers(),
                service.getPropertyMap()
        ));
    }

    @Override
    public void startService(String groupName) {
        final ServiceGroup group = groupManager.getServiceGroup(groupName);
        if (group == null) {
            return;
        }

        final int serviceId = getFreeServiceId(group);
        final int port = getServicePort(group);

        final ServiceImpl service = new ServiceImpl(
                serviceId,
                port,
                group,
                config,
                logger,
                server,
                screenManager,
                templateManager,
                platformManager,
                downloadManager,
                cacheManager,
                eventManager,
                this,
                console
        );

        services.add(service);

        server.broadcastPacket(new ServiceAddPacket(service.getName(),
                service.getServiceId(),
                service.getPort(),
                service.getStartTimestamp(),
                service.getGroup().getName(),
                service.getPropertyMap(),
                service.getStatus().name(),
                service.getMaxPlayers())
        );

        service.start();
    }

    @Override
    public void startServices(String groupName, int amount) {
        for (int i = 0; i < amount; i++) {
            startService(groupName);
        }
    }

    public void removeService(Service service) {
        services.remove(service);
    }

    private int getFreeServiceId(ServiceGroup group) {
        final Set<Integer> usedIds = services.stream()
                .filter(service -> service.getServiceGroup().equals(group))
                .map(Service::getServiceId)
                .collect(Collectors.toSet());

        int id = 1;
        while (usedIds.contains(id)) {
            id++;
        }
        return id;
    }

    private int getServicePort(ServiceGroup group) {
        int port = group.getPlatform().isProxy() ? config.getProxyStartPort() : config.getServiceStartPort();

        final Set<Integer> usedPorts = services.stream()
                .map(Service::getPort)
                .collect(Collectors.toSet());

        while (usedPorts.contains(port) || !NetworkUtils.isPortFree(port)) {
            port++;
        }

        return port;
    }

    @Override
    public Service getCurrentService() {
        return null;
    }
}
