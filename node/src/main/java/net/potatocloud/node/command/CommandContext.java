package net.potatocloud.node.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CommandContext {

    private final Map<String, Object> values = new HashMap<>();

    @Setter
    private CommandError error;

    public <T> void set(String name, T value) {
        values.put(name, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        return (T) values.get(name);
    }

    public boolean has(String key) {
        return values.containsKey(key);
    }

    public boolean isSuccess() {
        return error == null;
    }

    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static final class ParseResult {

        private final CommandContext context;
        private final int parsedArguments;
        private final boolean complete;
        private String errorMessage;

    }
}
