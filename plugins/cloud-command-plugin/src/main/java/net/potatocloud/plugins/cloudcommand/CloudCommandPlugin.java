package net.potatocloud.plugins.cloudcommand;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import net.potatocloud.plugins.cloudcommand.command.CloudCommand;

import java.util.logging.Logger;

public class CloudCommandPlugin {

    private final ProxyServer server;
    private final Logger logger;
    private final MessagesConfig messagesConfig;

    @Inject
    public CloudCommandPlugin(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
        this.messagesConfig = new MessagesConfig();
        messagesConfig.load();
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        server.getCommandManager().register(server.getCommandManager().metaBuilder("cloud").build(), new CloudCommand(messagesConfig));
    }
}

