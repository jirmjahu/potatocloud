package net.potatocloud.plugin.server.proxy.maintenance;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import lombok.RequiredArgsConstructor;
import net.potatocloud.plugin.server.proxy.ProxyPlugin;
import net.potatocloud.plugins.utils.Config;
import net.potatocloud.plugins.utils.MessagesConfig;

@RequiredArgsConstructor
public class LoginListener {

    private final ProxyPlugin plugin;
    private final Config config;
    private final MessagesConfig messages;

    @Subscribe
    public void handle(LoginEvent event) {
        final boolean isMaintenance = config.yaml().getBoolean("maintenance");

        if (!(isMaintenance)) {
            return;
        }

        final Player player = event.getPlayer();
        final String username = player.getUsername();

        if (plugin.getWhitelist().contains(username) || player.hasPermission("*")) {
            return;
        }

        event.setResult(ResultedEvent.ComponentResult.denied(messages.get("notWhitelist", false)));
    }
}
