package net.potatocloud.plugin.server.hub;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import net.potatocloud.plugin.server.hub.commands.HubCommand;
import net.potatocloud.plugin.server.shared.Config;
import net.potatocloud.plugin.server.shared.MessagesConfig;

import java.util.logging.Logger;

public class HubPlugin {

    private final ProxyServer server;
    private final Logger logger;
    private final Config config;
    private final MessagesConfig messagesConfig;

    @Inject
    public HubPlugin(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;

        final String folder = "plugins/potatocloud-hub";

        config = new Config(folder, "config.yml");
        messagesConfig = new MessagesConfig(folder);
        config.load();
        messagesConfig.load();
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        server.getCommandManager().register(server.getCommandManager().metaBuilder("hub")
                .aliases(commandAliases()).build(), new HubCommand(messagesConfig, server));
    }

    private String[] commandAliases() {
        return config.yaml().getStringList("aliases").toArray(new String[0]);
    }
}
