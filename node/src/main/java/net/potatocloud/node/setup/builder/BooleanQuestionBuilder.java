package net.potatocloud.node.setup.builder;

import net.potatocloud.node.setup.Question;
import net.potatocloud.node.setup.Setup;
import net.potatocloud.node.setup.questions.BooleanQuestion;

public class BooleanQuestionBuilder extends BaseQuestionBuilder {

    public BooleanQuestionBuilder(Setup parent, String name, String prompt) {
        super(parent, name, prompt);
    }

    @Override
    public Question question() {
        return new BooleanQuestion(name, prompt);
    }
}
