package net.potatocloud.node.setup.builder;

import net.potatocloud.node.setup.Question;
import net.potatocloud.node.setup.Setup;
import net.potatocloud.node.setup.questions.NumberQuestion;

public class NumberQuestionBuilder extends BaseQuestionBuilder {

    public NumberQuestionBuilder(Setup parent, String name, String prompt) {
        super(parent, name, prompt);
    }

    @Override
    public Question question() {
        return new NumberQuestion(name, prompt);
    }
}
