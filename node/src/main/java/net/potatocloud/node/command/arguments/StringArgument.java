package net.potatocloud.node.command.arguments;

import net.potatocloud.node.command.ArgumentType;

public class StringArgument extends ArgumentType<String> {

    public StringArgument(String name) {
        super(name);
    }

    @Override
    public ParseResult<String> parse(String input) {
        return ParseResult.success(input);
    }
}
