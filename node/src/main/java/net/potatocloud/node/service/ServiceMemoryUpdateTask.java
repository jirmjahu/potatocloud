package net.potatocloud.node.service;

import lombok.RequiredArgsConstructor;
import net.potatocloud.api.service.Service;
import net.potatocloud.core.networking.NetworkServer;
import net.potatocloud.core.networking.packet.packets.service.ServiceMemoryUpdatePacket;

@RequiredArgsConstructor
public class ServiceMemoryUpdateTask {

    private static final int UPDATE_INTERVAL = 2000;

    private final Service service;
    private final NetworkServer server;

    public void start() {
        final Thread thread = new Thread(() -> {
            while (service.isOnline()) {

                // Send current memory to the connector to keep it updated
                // We use a separate packet from ServiceUpdatePacket for performance because ServiceUpdatePacket contains stuff that does not need constant syncing
                server.generateBroadcast().broadcast(new ServiceMemoryUpdatePacket(service.getName(), service.getUsedMemory()));

                try {
                    Thread.sleep(UPDATE_INTERVAL);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }, "ServiceMemoryUpdateTask-" + service.getName());

        thread.setDaemon(true);
        thread.start();
    }
}
