package net.potatocloud.node.platform.steps;

import lombok.SneakyThrows;
import net.potatocloud.api.platform.Platform;
import net.potatocloud.api.platform.PrepareStep;
import net.potatocloud.api.service.Service;
import net.potatocloud.node.Node;
import net.potatocloud.node.config.NodeConfig;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

public class DefaultFilesStep implements PrepareStep {

    @Override
    @SneakyThrows
    public void execute(Service service, Platform platform, Path serverDirectory) {
        final NodeConfig config = Node.getInstance().getConfig();

        if (platform.isBukkit()) {
            final Path serverProperties = serverDirectory.resolve("server.properties");
            if (!serverProperties.toFile().exists()) {
                Files.copy(Path.of(config.getDataFolder(), "server.properties"), serverProperties);
            }

            final Path spigotYml = serverDirectory.resolve("spigot.yml");

            if (!Files.exists(spigotYml)) {
                Files.copy(Path.of(config.getDataFolder(), "spigot.yml"), spigotYml);
            }
        }

        if (platform.isVelocity()) {
            final Path velocityToml = serverDirectory.resolve("velocity.toml");
            if (!Files.exists(velocityToml)) {
                Files.copy(Path.of(config.getDataFolder(), "velocity.toml"), velocityToml);
            }

            // a forwarding secret file has to be created or else velocity will throw an error
            final Path forwardingSecret = serverDirectory.resolve("forwarding.secret");
            if (!Files.exists(forwardingSecret)) {
                Files.writeString(forwardingSecret, UUID.randomUUID().toString(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            }
        }
    }

    @Override
    public String getName() {
        return "default-files";
    }
}
