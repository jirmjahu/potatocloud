package net.potatocloud.plugin.server.proxy.tablist;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import lombok.RequiredArgsConstructor;
import net.labymod.serverapi.server.velocity.LabyModPlayer;
import net.labymod.serverapi.server.velocity.event.LabyModPlayerJoinEvent;
import net.potatocloud.plugin.server.shared.Config;

import java.util.Optional;

@RequiredArgsConstructor
public class TablistBannerHandler {

    private final Config config;

    @Subscribe
    public void onLabyModPlayerJoin(LabyModPlayerJoinEvent event) {
        final LabyModPlayer labyModPlayer = event.labyModPlayer();
        final Player player = labyModPlayer.getPlayer();
        final Optional<ServerConnection> serverConnection = player.getCurrentServer();

        if (serverConnection.isPresent()) {
            final String imageURL = config.yaml().getString("tablistImageURL");
            labyModPlayer.sendTabListBanner(imageURL);
        }
    }
}
