package net.potatocloud.plugin.server.shared;

import lombok.SneakyThrows;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

public class Config {

    private final String fileName;
    private final File file;
    private YamlFile yaml;

    public Config(String folder, String fileName) {
        this.fileName = fileName;
        this.file = new File(folder, fileName);
    }

    @SneakyThrows
    public void load() {
        if (!file.exists()) {
            // Load default file
            file.getParentFile().mkdirs();

            try (InputStream stream = getClass().getClassLoader().getResourceAsStream(fileName)) {
                if (stream == null) {
                    return;
                }
                Files.copy(stream, file.toPath());
            }
        }

        yaml = new YamlFile(file);
        yaml.loadWithComments();
    }

    @SneakyThrows
    public void save() {
        if (yaml == null) {
            return;
        }
        yaml.save();
    }

    public void reload() {
        load();
    }

    public YamlFile yaml() {
        return yaml;
    }
}
