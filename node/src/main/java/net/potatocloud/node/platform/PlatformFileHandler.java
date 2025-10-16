package net.potatocloud.node.platform;

import lombok.SneakyThrows;
import net.potatocloud.api.platform.Platform;
import net.potatocloud.api.platform.PlatformVersion;
import net.potatocloud.api.platform.impl.PlatformImpl;
import net.potatocloud.api.platform.impl.PlatformVersionImpl;
import net.potatocloud.node.console.Logger;
import org.apache.commons.io.FileUtils;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PlatformFileHandler {

    private final Logger logger;
    private final File file;
    private final YamlFile config;

    public PlatformFileHandler(Logger logger) {
        this.logger = logger;
        this.file = new File("platforms.yml");

        if (!file.exists()) {
            try (InputStream stream = getClass().getClassLoader().getResourceAsStream("platforms.yml")) {
                if (stream != null) {
                    FileUtils.copyInputStreamToFile(stream, file);
                }
            } catch (IOException e) {
                logger.error("Failed to copy platforms.yml file");
            }
        }

        this.config = mergePlatformsFile();
    }

    public List<Platform> loadPlatformsFile() {
        final List<Platform> platforms = new ArrayList<>();

        for (String key : config.getKeys(false)) {
            final ConfigurationSection section = config.getConfigurationSection(key);
            if (section == null) {
                continue;
            }

            final PlatformImpl platform = new PlatformImpl(
                    key,
                    section.getString("download"),
                    section.getBoolean("custom", false),
                    section.getBoolean("proxy", false),
                    section.getString("base", "UNKNOWN"),
                    section.getString("pre-cache"),
                    section.getString("parser", ""),
                    section.getString("hash-type", "")
            );

            platform.getPrepareSteps().addAll(new ArrayList<>(section.getStringList("prepare-steps")));

            // read versions of the platform
            final List<Map<?, ?>> versionMap = section.getMapList("versions");
            if (versionMap == null) {
                logger.warn("No versions found for platform " + key);
                continue;
            }

            final List<PlatformVersion> versions = new ArrayList<>();

            for (Map<?, ?> map : versionMap) {
                final String version = String.valueOf(map.get("version"));
                final String download = map.containsKey("download") ? String.valueOf(map.get("download")) : null;
                final boolean legacy = map.containsKey("legacy") && Boolean.parseBoolean(map.get("legacy").toString());

                versions.add(new PlatformVersionImpl(key, version, download, legacy));
            }

            platform.getVersions().addAll(versions);
            platforms.add(platform);
        }
        return platforms;
    }

    @SneakyThrows
    private YamlFile mergePlatformsFile() {
        // load the platforms file in the user directory
        final YamlFile userConfig = new YamlFile(file);
        userConfig.load();

        // now load the default platforms config
        final YamlFile defaultConfig = new YamlFile();
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("platforms.yml")) {
            if (stream != null) {
                defaultConfig.load(stream);
            }
        } catch (IOException e) {
            logger.error("Failed to copy platforms.yml file");
        }

        // merge user and default config so the user config is always up to date
        final YamlFile mergedConfig = new YamlFile();
        for (String key : userConfig.getKeys(false)) {
            final ConfigurationSection section = userConfig.getConfigurationSection(key);
            if (section == null) {
                continue;
            }
            // we want to keep custom platforms of the user so set them again in the merged config as well
            if (section.getBoolean("custom", false)) {
                mergedConfig.set(key, section);
            }
        }

        // set all other missing platforms
        for (String key : defaultConfig.getKeys(false)) {
            if (!mergedConfig.contains(key)) {
                mergedConfig.set(key, defaultConfig.get(key));
            }
        }

        // save the new merged config and replace the old user config with it
        mergedConfig.save(file);
        return mergedConfig;
    }

    @SneakyThrows
    public void updatePlatform(Platform platform) {
        final List<Map<String, Object>> versions = new ArrayList<>();

        for (PlatformVersion version : platform.getVersions()) {
            final Map<String, Object> versionInfoMap = new LinkedHashMap<>();

            versionInfoMap.put("version", version.getName());
            versionInfoMap.put("download", version.getDownloadUrl());
            versionInfoMap.put("legacy", true);

            versions.add(versionInfoMap);
        }

        config.set(platform.getName() + ".versions", versions);
        config.save(file);
    }
}
