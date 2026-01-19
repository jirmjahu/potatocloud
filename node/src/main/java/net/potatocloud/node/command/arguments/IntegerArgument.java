package net.potatocloud.node.command.arguments;

import net.potatocloud.node.command.ArgumentType;

import java.util.List;

public class IntegerArgument extends ArgumentType<Integer> {

    public IntegerArgument(String name) {
        super(name);
    }

    @Override
    public ParseResult<Integer> parse(String input) {
        try {
            return ParseResult.success(Integer.parseInt(input));
        } catch (NumberFormatException e) {
            return ParseResult.error("&cInvalid number: &a" + input);
        }
    }
}
