package net.potatocloud.node.command.arguments;

import net.potatocloud.api.player.CloudPlayer;
import net.potatocloud.node.Node;
import net.potatocloud.node.command.ArgumentType;

import java.util.List;

public class CloudPlayerArgument extends ArgumentType<CloudPlayer> {

    public CloudPlayerArgument(String name) {
        super(name);
    }

    @Override
    public ParseResult<CloudPlayer> parse(String input) {
        // Input is the username of the player in this case
        final CloudPlayer player = Node.getInstance().getPlayerManager().getCloudPlayer(input);
        if (player == null) {
            return ParseResult.error("&cNo player found with the name &a" + input);
        }

        return ParseResult.success(player);
    }

    @Override
    public List<String> suggest(String input) {
        return Node.getInstance()
                .getPlayerManager()
                .getOnlinePlayers()
                .stream()
                .map(CloudPlayer::getUsername)
                .filter(name -> name.startsWith(input))
                .toList();
    }
}
