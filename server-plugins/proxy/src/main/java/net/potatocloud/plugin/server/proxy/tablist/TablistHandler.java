package net.potatocloud.plugin.server.proxy.tablist;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.potatocloud.api.CloudAPI;
import net.potatocloud.api.player.CloudPlayer;
import net.potatocloud.api.service.Service;
import net.potatocloud.plugins.utils.Config;
import net.potatocloud.plugins.utils.MessageUtils;

@RequiredArgsConstructor
public class TablistHandler {

    private final Config config;
    private final ProxyServer server;

    @Subscribe
    public void onServerPostConnection(ServerPostConnectEvent event) {
        server.getAllPlayers().forEach(this::update);
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        server.getAllPlayers().forEach(this::update);
    }

    @Subscribe
    public void onPlayerChooseInitialServer(PlayerChooseInitialServerEvent event) {
        this.update(event.getPlayer());
    }

    private void update(Player player) {
        final CloudPlayer cloudPlayer = CloudAPI.getInstance().getPlayerManager().getCloudPlayer(player.getUsername());
        if (cloudPlayer == null) {
            return;
        }

        final Service service = CloudAPI.getInstance().getServiceManager().getService(cloudPlayer.getConnectedServiceName());
        if (service == null || service.getServiceGroup() == null) {
            return;
        }

        final String group = service.getServiceGroup().getName();
        final String proxy = cloudPlayer.getConnectedProxyName();

        final int onlinePlayers = CloudAPI.getInstance().getPlayerManager().getOnlinePlayers().size();
        final int maxPlayers = CloudAPI.getInstance().getServiceManager().getCurrentService().getMaxPlayers();

        final Tablist tablist = new Tablist(
                config.yaml().getString("tablist.header"),
                config.yaml().getString("tablist.footer")
        );

        final Component header = replacePlaceholders(tablist.header(), service.getName(), group,
                proxy, onlinePlayers, maxPlayers);

        final Component footer = replacePlaceholders(tablist.footer(), service.getName(), group,
                proxy, onlinePlayers, maxPlayers);

        player.sendPlayerListHeaderAndFooter(header, footer);
    }

    private Component replacePlaceholders(String text, String service, String group, String proxy, int onlinePlayers, int maxPlayers) {
        return MessageUtils.format(text)
                .replaceText(b -> b.match("%service%").replacement(service))
                .replaceText(b -> b.match("%group%").replacement(group))
                .replaceText(b -> b.match("%proxy%").replacement(proxy))
                .replaceText(b -> b.match("%online_players%").replacement(String.valueOf(onlinePlayers)))
                .replaceText(b -> b.match("%max_players%").replacement(String.valueOf(maxPlayers)));
    }
}
