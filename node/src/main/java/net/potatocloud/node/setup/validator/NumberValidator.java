package net.potatocloud.node.setup.validator;

import net.potatocloud.node.setup.AnswerResult;

public class NumberValidator implements AnswerValidator {

    @Override
    public AnswerResult validateInput(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return AnswerResult.error("Please enter a valid number");
        }
        return AnswerResult.success();
    }

}
