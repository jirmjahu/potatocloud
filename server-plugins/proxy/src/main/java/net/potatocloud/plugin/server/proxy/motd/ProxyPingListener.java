package net.potatocloud.plugin.server.proxy.motd;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.potatocloud.api.CloudAPI;
import net.potatocloud.plugin.server.proxy.ProxyPlugin;
import net.potatocloud.plugins.utils.Config;
import net.potatocloud.plugins.utils.MessageUtils;

@RequiredArgsConstructor
public class ProxyPingListener {

    private final ProxyPlugin plugin;
    private final Config config;

    @Subscribe
    public void onPing(ProxyPingEvent event) {
        event.setPing(serverPing(event.getPing()));
    }

    private ServerPing serverPing(ServerPing ping) {
        final int onlinePlayers = CloudAPI.getInstance().getPlayerManager().getOnlinePlayers().size();
        final int maxPlayers = CloudAPI.getInstance().getServiceManager().getCurrentService().getMaxPlayers();

        final Motd motd = plugin.isMaintenance() ? maintenanceMotd() : defaultMotd();

        if (motd.version() == null) {
            return ping.asBuilder()
                    .onlinePlayers(onlinePlayers)
                    .maximumPlayers(maxPlayers)
                    .description(MessageUtils.format(motd.firstLine())
                            .append(Component.text("\n"))
                            .append(MessageUtils.format(motd.secondLine())))
                    .build();
        }

        return ping.asBuilder()
                .onlinePlayers(onlinePlayers)
                .maximumPlayers(maxPlayers)
                .description(MessageUtils.format(motd.firstLine())
                        .append(Component.text("\n"))
                        .append(MessageUtils.format(motd.secondLine())))
                .version(new ServerPing.Version(-1, LegacyComponentSerializer.legacySection().serialize(MessageUtils.format(motd.version()))))
                .build();
    }

    private Motd defaultMotd() {
        return new Motd(config.yaml().getString("motd.default.firstLine"),
                config.yaml().getString("motd.default.secondLine"));
    }

    private Motd maintenanceMotd() {
        return new Motd(config.yaml().getString("motd.maintenance.firstLine"),
                config.yaml().getString("motd.maintenance.secondLine"),
                config.yaml().getString("motd.maintenance.version"));
    }
}
