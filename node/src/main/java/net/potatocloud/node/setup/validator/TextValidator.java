package net.potatocloud.node.setup.validator;

import net.potatocloud.node.setup.AnswerResult;

public class TextValidator implements AnswerValidator {

    @Override
    public AnswerResult validateInput(String input) {
        if (input.isBlank()) {
            return AnswerResult.error("The input cannot be blank");
        }
        return AnswerResult.success();
    }
}
