package net.potatocloud.node.service;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.potatocloud.node.config.NodeConfig;
import net.potatocloud.node.console.Logger;
import org.apache.commons.io.FileUtils;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@UtilityClass
public class ServiceDefaultFiles {

    @SneakyThrows
    public void copyDefaultFiles(Logger logger, NodeConfig config, ClassLoader classLoader) {
        final Path dataFolder = Path.of(config.getDataFolder());
        Files.createDirectories(dataFolder);

        final List<String> files = List.of(
                "server.properties",
                "spigot.yml",
                "paper-global.yml",
                "velocity.toml",
                "limbo-server.properties",
                "potatocloud-plugin-spigot.jar",
                "potatocloud-plugin-spigot-legacy.jar",
                "potatocloud-plugin-velocity.jar",
                "potatocloud-plugin-limbo.jar"
        );

        for (String name : files) {
            try (InputStream stream = classLoader.getResourceAsStream("default-files/" + name)) {
                if (stream == null) {
                    logger.error("Default file not found in resources: " + name);
                    continue;
                }

                FileUtils.copyInputStreamToFile(stream, dataFolder.resolve(name).toFile());
            } catch (Exception e) {
                logger.error("Failed to copy default service file: " + name);
            }
        }
    }
}
