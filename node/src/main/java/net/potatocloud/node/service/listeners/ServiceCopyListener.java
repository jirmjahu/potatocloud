package net.potatocloud.node.service.listeners;

import lombok.RequiredArgsConstructor;
import net.potatocloud.api.service.Service;
import net.potatocloud.api.service.ServiceManager;
import net.potatocloud.core.networking.NetworkConnection;
import net.potatocloud.core.networking.packet.PacketListener;
import net.potatocloud.core.networking.packet.packets.service.ServiceCopyPacket;

@RequiredArgsConstructor
public class ServiceCopyListener implements PacketListener<ServiceCopyPacket> {

    private final ServiceManager serviceManager;

    @Override
    public void onPacket(NetworkConnection connection, ServiceCopyPacket packet) {
        final Service service = serviceManager.getService(packet.getServiceName());
        if (service == null) {
            return;
        }
        service.copy(packet.getTemplateName(), packet.getFilter());
    }
}
