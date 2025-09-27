package net.potatocloud.node.command;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public abstract class Command {

    private final String name;
    private final String description;
    private final List<String> aliases;

    protected Command() {
        final CommandInfo info = this.getClass().getAnnotation(CommandInfo.class);
        if (info == null) {
            throw new IllegalStateException("CommandInfo annotation missing in Command: " + getClass().getSimpleName());
        }
        name = info.name();
        description = info.description();
        aliases = Arrays.asList(info.aliases());
    }

    public abstract void execute(String[] args);

}
