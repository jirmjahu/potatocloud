package net.potatocloud.node.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.potatocloud.api.group.ServiceGroup;
import net.potatocloud.api.platform.Platform;
import net.potatocloud.api.service.Service;
import net.potatocloud.node.command.arguments.*;

import java.util.List;

@Getter
@AllArgsConstructor
public abstract class ArgumentType<T> {

    private final String name;
    private final boolean required;

    public ArgumentType(String name) {
        this.name = name;
        this.required = true;
    }

    public abstract ParseResult<T> parse(String input);

    public List<String> suggest(String input) {
        return List.of();
    }

    public static ArgumentType<String> String(String name) {
        return new StringArgument(name);
    }

    public static ArgumentType<String> MultiString(String name) {
        return new MultiStringArgument(name);
    }

    public static ArgumentType<Integer> Integer(String name) {
        return new IntegerArgument(name);
    }

    public static ArgumentType<ServiceGroup> Group(String name) {
        return new ServiceGroupArgument(name);
    }

    public static ArgumentType<Service> Service(String name) {
        return new ServiceArgument(name);
    }

    public static ArgumentType<Platform> Platform(String name) {
        return new PlatformArgument(name);
    }

    public ArgumentType<T> asOptionalArgument() {
        return new Optional<>(this);
    }

    public static class Optional<T> extends ArgumentType<T> {

        private final ArgumentType<T> argument;

        public Optional(ArgumentType<T> argument) {
            super(argument.getName(), false);
            this.argument = argument;
        }

        @Override
        public ParseResult<T> parse(String input) {
            return argument.parse(input);
        }
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
