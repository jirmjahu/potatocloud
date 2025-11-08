package net.potatocloud.node.setup.validator;

import net.potatocloud.node.setup.AnswerResult;

public class TextValidator implements AnswerValidator {

    @Override
    public AnswerResult validateInput(String input) {
        if (input.isBlank()) {
            return AnswerResult.error("Please enter a valid text");
        }
        return AnswerResult.success();
    }
}
