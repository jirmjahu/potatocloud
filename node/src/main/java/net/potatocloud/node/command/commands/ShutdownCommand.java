package net.potatocloud.node.command.commands;

import net.potatocloud.node.Node;
import net.potatocloud.node.command.Command;
import net.potatocloud.node.command.CommandInfo;

@CommandInfo(name = "shutdown", description = "Shutdown the node", aliases = {"stop", "end"})
public class ShutdownCommand extends Command {

    public ShutdownCommand(Node node) {
        defaultExecutor(ctx -> node.shutdown());
    }
}
