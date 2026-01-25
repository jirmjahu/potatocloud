package net.potatocloud.node.command;

import java.util.List;

public interface CommandCompleter {

    List<String> complete(CommandContext ctx, String input, int argsLength);

}
