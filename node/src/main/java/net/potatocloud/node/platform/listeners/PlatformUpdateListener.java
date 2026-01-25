package net.potatocloud.node.platform.listeners;

import lombok.RequiredArgsConstructor;
import net.potatocloud.api.platform.Platform;
import net.potatocloud.api.platform.PlatformManager;
import net.potatocloud.core.networking.NetworkConnection;
import net.potatocloud.core.networking.packet.PacketListener;
import net.potatocloud.core.networking.packet.packets.platform.PlatformUpdatePacket;

@RequiredArgsConstructor
public class PlatformUpdateListener implements PacketListener<PlatformUpdatePacket> {

    private final PlatformManager platformManager;

    @Override
    public void onPacket(NetworkConnection connection, PlatformUpdatePacket packet) {
        final Platform platform = platformManager.getPlatform(packet.getPlatform().getName());
        if (platform == null) {
            return;
        }
        platform.setVersions(packet.getPlatform().getVersions());
    }
}
