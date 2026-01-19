package net.potatocloud.node.command.arguments;

import net.potatocloud.node.command.ArgumentType;

import java.util.Arrays;

public class MultiStringArgument extends ArgumentType<String> {

    public MultiStringArgument(String name) {
        super(name);
    }

    @Override
    public ParseResult<String> parse(String input) {
        return ParseResult.success(input);
    }

    public String combineArguments(String[] args, int startIndex) {
        if (startIndex >= args.length) {
            return "";
        }
        return String.join(" ", Arrays.copyOfRange(args, startIndex, args.length));
    }
}
