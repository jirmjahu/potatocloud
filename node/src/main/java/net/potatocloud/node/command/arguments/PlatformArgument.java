package net.potatocloud.node.command.arguments;

import net.potatocloud.api.platform.Platform;
import net.potatocloud.node.Node;
import net.potatocloud.node.command.ArgumentType;

import java.util.List;

public class PlatformArgument extends ArgumentType<Platform> {

    public PlatformArgument(String name) {
        super(name);
    }

    @Override
    public ParseResult<Platform> parse(String input) {
        final Platform platform = Node.getInstance().getPlatformManager().getPlatform(input);
        if (platform == null) {
            return ParseResult.error("&cNo platform found with the name &a" + input);
        }

        return ParseResult.success(platform);
    }

    @Override
    public List<String> suggest(String input) {
        return Node.getInstance()
                .getPlatformManager()
                .getPlatforms()
                .stream()
                .map(Platform::getName)
                .filter(name -> name.startsWith(input))
                .toList();
    }
}
