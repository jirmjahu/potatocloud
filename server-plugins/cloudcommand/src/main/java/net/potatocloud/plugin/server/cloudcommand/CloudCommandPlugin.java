package net.potatocloud.plugin.server.cloudcommand;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import net.potatocloud.plugin.server.cloudcommand.command.CloudCommand;
import net.potatocloud.plugin.server.shared.Config;
import net.potatocloud.plugin.server.shared.MessagesConfig;

import java.util.logging.Logger;

public class CloudCommandPlugin {

    private final ProxyServer server;
    private final Logger logger;
    private final Config config;
    private final MessagesConfig messagesConfig;

    @Inject
    public CloudCommandPlugin(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;

        final String folder = "plugins/potatocloud-cloudcommand";

        config = new Config(folder, "config.yml");
        messagesConfig = new MessagesConfig(folder);

        config.load();
        messagesConfig.load();
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        server.getCommandManager().register(server.getCommandManager().metaBuilder("cloud")
                .aliases(commandAliases()).build(), new CloudCommand(config, messagesConfig));
    }

    private String[] commandAliases() {
        return config.yaml().getStringList("aliases").toArray(new String[0]);
    }
}

