package net.potatocloud.node.command.commands;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.potatocloud.api.platform.Platform;
import net.potatocloud.api.platform.PlatformManager;
import net.potatocloud.api.platform.PlatformVersion;
import net.potatocloud.node.command.Command;
import net.potatocloud.node.command.CommandInfo;
import net.potatocloud.node.command.TabCompleter;
import net.potatocloud.node.console.Logger;
import net.potatocloud.node.platform.DownloadManager;

import java.nio.file.Path;
import java.util.List;

@RequiredArgsConstructor
@CommandInfo(name = "platform", description = "Manage your platforms", aliases = {"platforms"})
public class PlatformCommand extends Command implements TabCompleter {

    private final Logger logger;
    private final Path platformsFolder;
    private final PlatformManager platformManager;
    private final DownloadManager downloadManager;

    @SneakyThrows
    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            sendHelp();
            return;
        }

        final String sub = args[0].toLowerCase();

        switch (sub) {
            case "list" -> listPlatforms();
            case "download" -> downloadPlatform(args);
            default -> {
                sendHelp();
            }
        }
    }

    private void listPlatforms() {
        logger.info("&7Available platforms&8:");
        for (Platform platform : platformManager.getPlatforms()) {
            logger.info("&8» &a" + platform.getName() +
                    " &7- Proxy: &a" + platform.isProxy() +
                    " &7- Custom: &a" + platform.isCustom());
        }
    }

    @SneakyThrows
    private void downloadPlatform(String[] args) {
        if (args.length < 3) {
            logger.info("&cUsage&8: &7platform download &8[&aplatform&8] &8[&aversion&8]");
            return;
        }

        final Platform platform = platformManager.getPlatform(args[1]);
        if (platform == null) {
            logger.info("This platform does &cnot &7exist");
            return;
        }

        final PlatformVersion version = platform.getVersion(args[2]);
        if (version == null) {
            logger.info("This version does &7not &7exist for the given platform");
            return;
        }

        downloadManager.downloadPlatformVersion(platform, version);
    }

    private void sendHelp() {
        logger.info("&7platform list &8- &7List all available platforms");
        logger.info("&7platform download &8[&aplatform&8] &8[&aversion&8] &7- Download a platform");
    }

    @Override
    public List<String> complete(String[] args) {
        if (args.length == 1) {
            return List.of("list", "download").stream()
                    .filter(input -> input.startsWith(args[0].toLowerCase()))
                    .toList();
        }

        final String sub = args[0].toLowerCase();

        if (sub.equals("download")) {
            if (args.length == 2) {
                return platformManager.getPlatforms().stream()
                        .map(Platform::getName)
                        .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                        .toList();
            }
            if (args.length == 3) {
                final Platform platform = platformManager.getPlatform(args[1]);
                if (platform != null) {
                    return platform.getVersions().stream()
                            .map(PlatformVersion::getName)
                            .filter(ver -> ver.toLowerCase().startsWith(args[2].toLowerCase()))
                            .toList();
                }
            }
        }

        return List.of();
    }
}
