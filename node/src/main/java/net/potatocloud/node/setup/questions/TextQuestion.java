package net.potatocloud.node.setup.questions;

import lombok.Getter;
import lombok.Setter;
import net.potatocloud.node.setup.AbstractQuestion;
import net.potatocloud.node.setup.validator.AnswerValidator;
import net.potatocloud.node.setup.validator.TextValidator;

@Getter
@Setter
public class TextQuestion extends AbstractQuestion {

    public TextQuestion(String name, String prompt) {
        super(name, prompt);
    }

    @Override
    public AnswerValidator getDefaultValidator() {
        return new TextValidator();
    }
}
