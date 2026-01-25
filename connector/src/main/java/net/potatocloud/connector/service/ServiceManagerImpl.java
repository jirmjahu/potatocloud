package net.potatocloud.connector.service;

import net.potatocloud.api.service.Service;
import net.potatocloud.api.service.ServiceManager;
import net.potatocloud.connector.service.listeners.ServiceAddListener;
import net.potatocloud.connector.service.listeners.ServiceMemoryUpdateListener;
import net.potatocloud.connector.service.listeners.ServiceUpdateListener;
import net.potatocloud.core.networking.NetworkClient;
import net.potatocloud.core.networking.packet.packets.service.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServiceManagerImpl implements ServiceManager {

    private final List<Service> services = new CopyOnWriteArrayList<>();
    private final NetworkClient client;

    public ServiceManagerImpl(NetworkClient client) {
        this.client = client;

        client.send(new RequestServicesPacket());

        client.on(ServiceAddPacket.class, new ServiceAddListener(this));

        client.on(ServiceRemovePacket.class, (connection, packet) -> {
            services.remove(getService(packet.getServiceName()));
        });

        client.on(ServiceUpdatePacket.class, new ServiceUpdateListener(this));

        client.on(ServiceMemoryUpdatePacket.class, new ServiceMemoryUpdateListener(this));
    }

    public void addService(Service service) {
        services.add(service);
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
        client.send(new ServiceUpdatePacket(
                service.getName(),
                service.getStatus().name(),
                service.getMaxPlayers(),
                service.getPropertyMap())
        );
    }

    @Override
    public void startService(String groupName) {
        client.send(new StartServicePacket(groupName));
    }

    @Override
    public void startServices(String groupName, int amount) {
        for (int i = 0; i < amount; i++) {
            startService(groupName);
        }
    }

    @Override
    public Service getCurrentService() {
        return getService(System.getProperty("potatocloud.service.name"));
    }
}