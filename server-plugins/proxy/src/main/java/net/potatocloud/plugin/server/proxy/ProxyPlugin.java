package net.potatocloud.plugin.server.proxy;

import com.google.inject.Inject;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import net.labymod.serverapi.server.velocity.LabyModProtocolService;
import net.potatocloud.plugin.server.proxy.commands.ProxyCommand;
import net.potatocloud.plugin.server.proxy.maintenance.LoginListener;
import net.potatocloud.plugin.server.proxy.motd.ProxyPingListener;
import net.potatocloud.plugin.server.proxy.tablist.TablistBannerHandler;
import net.potatocloud.plugin.server.proxy.tablist.TablistHandler;
import net.potatocloud.plugins.utils.Config;
import net.potatocloud.plugins.utils.MessagesConfig;
import org.slf4j.Logger;

import java.util.List;

public class ProxyPlugin {

    private final ProxyServer server;
    private final Logger logger;
    private final MessagesConfig messagesConfig;
    private final Config config;

    @Inject
    public ProxyPlugin(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
        final String folder = "plugins/potatocloud-proxy";

        config = new Config(folder, "config.yml");
        messagesConfig = new MessagesConfig(folder);
        config.load();
        messagesConfig.load();
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        LabyModProtocolService.initialize(this, server, logger);

        final EventManager eventManager = server.getEventManager();

        if (config.yaml().getBoolean("useMotd")) {
            eventManager.register(this, new ProxyPingListener(this, config));
        }

        if (config.yaml().getBoolean("useTablist")) {
            eventManager.register(this, new TablistHandler(config, server));
        }
        if (config.yaml().getBoolean("useTablistBanner")) {
            eventManager.register(this, new TablistBannerHandler(config));
        }

        eventManager.register(this, new LoginListener(this, config, messagesConfig));

        server.getCommandManager().register(server.getCommandManager().metaBuilder("proxy").build(), new ProxyCommand(this, config, messagesConfig));
    }

    public boolean isMaintenance() {
        return config.yaml().getBoolean("maintenance");
    }

    public List<String> getWhitelist() {
        return config.yaml().getStringList("whitelist");
    }

    public void setWhitelist(List<String> whitelist) {
        config.yaml().set("whitelist", whitelist);
        config.save();
    }

    public void setMaintenance(boolean maintenance) {
        config.yaml().set("maintenance", maintenance);
        config.save();
    }
}
