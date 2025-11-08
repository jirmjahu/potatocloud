package net.potatocloud.node.setup.validator;

import net.potatocloud.node.setup.AnswerResult;

public interface AnswerValidator {

    AnswerResult validateInput(String input);

}
