package net.potatocloud.node.command.commands;

import net.potatocloud.node.command.Command;
import net.potatocloud.node.command.CommandInfo;
import net.potatocloud.node.console.Console;

@CommandInfo(name = "clear", description = "Clears console screen", aliases = {"cls"})
public class ClearCommand extends Command {

    public ClearCommand(Console console) {
        defaultExecutor(ctx -> console.clearScreen());
    }
}
