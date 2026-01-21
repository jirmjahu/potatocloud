package net.potatocloud.connector.service.listeners;

import lombok.RequiredArgsConstructor;
import net.potatocloud.api.service.Service;
import net.potatocloud.api.service.ServiceManager;
import net.potatocloud.connector.service.ServiceImpl;
import net.potatocloud.core.networking.NetworkConnection;
import net.potatocloud.core.networking.packet.PacketListener;
import net.potatocloud.core.networking.packet.packets.service.ServiceMemoryUpdatePacket;

@RequiredArgsConstructor
public class ServiceMemoryUpdateListener implements PacketListener<ServiceMemoryUpdatePacket> {

    private final ServiceManager serviceManager;

    @Override
    public void onPacket(NetworkConnection connection, ServiceMemoryUpdatePacket packet) {
        final Service service = serviceManager.getService(packet.getServiceName());
        if (service == null) {
            return;
        }

        if (service instanceof ServiceImpl serviceImpl) {
            serviceImpl.setUsedMemory(packet.getUsedMemory());
        }
    }
}
