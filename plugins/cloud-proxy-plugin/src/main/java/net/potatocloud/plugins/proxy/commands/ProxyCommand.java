package net.potatocloud.plugins.proxy.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import lombok.RequiredArgsConstructor;
import net.potatocloud.plugins.proxy.Config;
import net.potatocloud.plugins.proxy.MessagesConfig;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class ProxyCommand implements SimpleCommand {

    private final Config config;
    private final MessagesConfig messagesConfig;

    @Override
    public void execute(Invocation invocation) {
        final CommandSource source = invocation.source();
        final String[] args = invocation.arguments();

        if (!(source instanceof Player player)) {
            return;
        }

        if (!(player.hasPermission(this.config.getPermission()))) {
            player.sendMessage(this.messagesConfig.get("no-permission"));
            return;
        }

        if (args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "toggle" -> {
                    boolean state = this.config.maintenance();
                    boolean newState = !state;
                    this.config.maintenance(newState);

                    player.sendMessage(newState ? this.messagesConfig.get("now_maintenance") : this.messagesConfig.get("now_not_maintenance"));
                }
                case "list", "info" -> {
                    player.sendMessage(this.config.maintenance() ? this.messagesConfig.get("now_maintenance") : this.messagesConfig.get("now_not_maintenance"));
                    player.sendMessage(this.messagesConfig.get("info_text"));
                    this.config.whitelist().forEach(name -> {
                        player.sendMessage(this.messagesConfig.get("info_key")
                                .replaceText(text -> text.match("%name%").replacement(name)));
                    });
                }
                case "reload" -> {
                    this.config.reload();
                    player.sendMessage(this.messagesConfig.get("reload"));
                }
                default -> {
                    this.usage(player);
                }
            }
            return;
        }

        if (args.length == 3) {
            if (!args[0].equalsIgnoreCase("whitelist")) {
                this.usage(player);
                return;
            }

            if (args[1].equalsIgnoreCase("add")) {
                List<String> whitelist = this.config.whitelist();
                if (whitelist.contains(args[2])) {
                    player.sendMessage(this.messagesConfig.get("whitelist.already"));
                    return;
                }
                whitelist.add(args[2]);
                this.config.whitelist(whitelist);
                player.sendMessage(this.messagesConfig.get("whitelist.added"));
                return;
            }

            if (args[1].equalsIgnoreCase("remove")) {
                List<String> whitelist = this.config.whitelist();
                if (!(whitelist.contains(args[2]))) {
                    player.sendMessage(this.messagesConfig.get("whitelist.not")
                            .replaceText(text -> text.match("%name%").replacement(args[2])));
                    return;
                }
                whitelist.remove(args[2]);
                this.config.whitelist(whitelist);
                player.sendMessage(this.messagesConfig.get("whitelist.removed")
                        .replaceText(text -> text.match("%name%").replacement(args[2])));
                return;
            }

            this.usage(player);
            return;
        }

        this.usage(player);
    }

    private void usage(Player player) {
        player.sendMessage(this.messagesConfig.get("help.toggle"));
        player.sendMessage(this.messagesConfig.get("help.list"));
        player.sendMessage(this.messagesConfig.get("help.reload"));
        player.sendMessage(this.messagesConfig.get("help.whitelist"));
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        final CommandSource source = invocation.source();
        final String[] args = invocation.arguments();

        if (!(source instanceof Player player)) {
            return Collections.emptyList();
        }

        if (!(player.hasPermission(this.config.getPermission()))) {
            return Collections.emptyList();
        }

        if (args.length == 3) {
            if (args[1].equalsIgnoreCase("remove")) {
                return this.config.whitelist();
            }
        }

        if (args.length == 2) {
            return List.of("add", "remove");
        }

        if (args.length == 0 || args.length == 1) {
            return List.of("toggle", "list", "whitelist", "reload");
        }


        return Collections.emptyList();
    }
}
