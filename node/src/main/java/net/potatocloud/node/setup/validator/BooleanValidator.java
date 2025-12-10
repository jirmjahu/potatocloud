package net.potatocloud.node.setup.validator;

import net.potatocloud.node.setup.AnswerResult;

public class BooleanValidator implements AnswerValidator {

    @Override
    public AnswerResult validateInput(String input) {
        final String lower = input.toLowerCase();

        if (!lower.equals("true") && !lower.equals("false") && !lower.equals("yes") && !lower.equals("no")) {
            return AnswerResult.error("Please enter yes/true or no/false");
        }

        return AnswerResult.success();
    }
}
