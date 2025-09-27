package net.potatocloud.node.command.commands;

import lombok.RequiredArgsConstructor;
import net.potatocloud.api.player.CloudPlayer;
import net.potatocloud.api.player.CloudPlayerManager;
import net.potatocloud.api.service.Service;
import net.potatocloud.api.service.ServiceManager;
import net.potatocloud.node.command.Command;
import net.potatocloud.node.command.CommandInfo;
import net.potatocloud.node.command.TabCompleter;
import net.potatocloud.node.console.Logger;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@CommandInfo(name = "player", description = "Manage players", aliases = {"players", "cloudplayer"})
public class PlayerCommand extends Command implements TabCompleter {

    private final Logger logger;
    private final CloudPlayerManager playerManager;
    private final ServiceManager serviceManager;

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            sendHelp();
            return;
        }

        final String sub = args[0];

        if (sub.equalsIgnoreCase("list")) {
            final Set<CloudPlayer> players = playerManager.getOnlinePlayers();
            if (players.isEmpty()) {
                logger.info("There are &cno &7online players");
                return;
            }
            for (CloudPlayer player : players) {
                logger.info("&8» &a" + player.getUsername() + " &7- Proxy: &a" + player.getConnectedProxyName() + " &7- Service: &a" + player.getConnectedServiceName());
            }
            return;
        }

        if (sub.equalsIgnoreCase("connect")) {
            if (args.length < 3) {
                logger.info("&cUsage&8: &7player connect &8[&aplayerName&8] [&aserviceName&8]");
                return;
            }
            final String playerName = args[1];
            final String serviceName = args[2];

            final CloudPlayer player = playerManager.getCloudPlayer(playerName);
            if (player == null) {
                logger.info("&cNo player found with the name &a" + playerName);
                return;
            }

            final Service service = serviceManager.getService(serviceName);
            if (service == null) {
                logger.info("&cNo service found with the name &a" + serviceName);
                return;
            }

            if (player.getConnectedServiceName().equalsIgnoreCase(service.getName())) {
                logger.info("Player &a" + player.getUsername() + " &7is already connected to &a" + service.getName());
                return;
            }

            player.connectWithService(service);
            logger.info("Successfully connected player &a" + player.getUsername() + " &7to service &a" + service.getName());
        }
    }

    private void sendHelp() {
        logger.info("player list &8- &7List all online players");
        logger.info("player connect &8[&aplayerName&8] [&aserviceName&8] - &7Connect a player to a service");
    }

    @Override
    public List<String> complete(String[] args) {
        if (args.length == 1) {
            return List.of("list", "connect").stream()
                    .filter(input -> input.startsWith(args[0].toLowerCase()))
                    .toList();
        }

        final String sub = args[0].toLowerCase();

        if (sub.equalsIgnoreCase("connect")) {
            if (args.length == 2) {
                return playerManager.getOnlinePlayers().stream().map(CloudPlayer::getUsername).
                        filter(input -> input.startsWith(args[1])).toList();
            }
            if (args.length == 3) {
                return serviceManager.getAllServices().stream()
                        .filter(service -> !service.getServiceGroup().getPlatform().isProxy())
                        .map(Service::getName)
                        .filter(name -> name.startsWith(args[2]))
                        .toList();
            }
        }

        return List.of();
    }
}
