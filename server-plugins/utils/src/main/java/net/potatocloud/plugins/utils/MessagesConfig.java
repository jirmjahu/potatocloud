package net.potatocloud.plugins.utils;

import net.kyori.adventure.text.Component;

public class MessagesConfig extends Config {

    public MessagesConfig(String folder) {
        super(folder, "messages.yml");
    }

    public Component get(String key, boolean usePrefix) {
        String message = yaml().getString(key);
        if (message == null) {
            message = "";
        }

        String prefix = "";
        final String rawPrefix = yaml().getString("prefix");
        if (rawPrefix != null) {
            prefix = usePrefix ? rawPrefix : "";
        }

        return MessageUtils.format(prefix + message);
    }

    public Component get(String key) {
        return get(key, true);
    }
}
