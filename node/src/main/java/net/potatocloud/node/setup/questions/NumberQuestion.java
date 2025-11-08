package net.potatocloud.node.setup.questions;

import lombok.Getter;
import lombok.Setter;
import net.potatocloud.node.setup.AbstractQuestion;
import net.potatocloud.node.setup.validator.AnswerValidator;
import net.potatocloud.node.setup.validator.NumberValidator;

@Getter
@Setter
public class NumberQuestion extends AbstractQuestion {

    public NumberQuestion(String name, String prompt) {
        super(name, prompt);
    }

    @Override
    public AnswerValidator getDefaultValidator() {
        return new NumberValidator();
    }
}
