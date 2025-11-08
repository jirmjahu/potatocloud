package net.potatocloud.node.setup;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AnswerResult {

    private final boolean success;
    private final String errorMessage;

    public static AnswerResult success() {
        return new AnswerResult(true, null);
    }

    public static AnswerResult error(String errorMessage) {
        return new AnswerResult(false, errorMessage);
    }
}