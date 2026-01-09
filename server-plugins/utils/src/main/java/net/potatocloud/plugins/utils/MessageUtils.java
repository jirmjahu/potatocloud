package net.potatocloud.plugins.utils;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

@UtilityClass
public class MessageUtils {

    public Component format(String text) {
        return MiniMessage.miniMessage().deserialize(text);
    }
}
