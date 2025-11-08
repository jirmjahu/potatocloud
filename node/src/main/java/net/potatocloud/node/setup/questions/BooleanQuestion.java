package net.potatocloud.node.setup.questions;

import lombok.Getter;
import lombok.Setter;
import net.potatocloud.node.setup.AbstractQuestion;
import net.potatocloud.node.setup.validator.AnswerValidator;
import net.potatocloud.node.setup.validator.BooleanValidator;

@Getter
@Setter
public class BooleanQuestion extends AbstractQuestion {

    public BooleanQuestion(String name, String prompt) {
        super(name, prompt);
    }

    @Override
    public AnswerValidator getDefaultValidator() {
        return new BooleanValidator();
    }
}
