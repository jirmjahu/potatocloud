package net.potatocloud.node.platform.listeners;

import lombok.RequiredArgsConstructor;
import net.potatocloud.core.networking.NetworkConnection;
import net.potatocloud.core.networking.packet.PacketListener;
import net.potatocloud.core.networking.packet.packets.platform.PlatformAddPacket;
import net.potatocloud.node.platform.PlatformManagerImpl;

@RequiredArgsConstructor
public class PlatformAddListener implements PacketListener<PlatformAddPacket> {

    private final PlatformManagerImpl platformManager;

    @Override
    public void onPacket(NetworkConnection connection, PlatformAddPacket packet) {
        platformManager.addPlatform(packet.getPlatform());
    }
}
