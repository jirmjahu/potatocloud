package net.potatocloud.node.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.potatocloud.api.group.ServiceGroup;
import net.potatocloud.node.command.arguments.ServiceGroupArgument;
import net.potatocloud.node.command.arguments.StringArgument;

import java.util.List;

@Getter
@RequiredArgsConstructor
public abstract class ArgumentType<T> {

    private final String name;

    public abstract ParseResult<T> parse(String input);

    public List<String> suggest(String input) {
        return List.of();
    }

    public static ArgumentType<ServiceGroup> Group(String name) {
        return new ServiceGroupArgument(name);
    }

    public static ArgumentType<String> String(String name) {
        return new StringArgument(name);
    }

    @Getter
    @RequiredArgsConstructor
    public static class ParseResult<T> {

        private final T value;
        private final CommandError error;

        public static <T> ParseResult<T> success(T value) {
            return new ParseResult<>(value, null);
        }

        public static <T> ParseResult<T> error(String message) {
            return new ParseResult<>(null, new CommandError(message));
        }

        public boolean isSuccess() {
            return error == null;
        }
    }

}
