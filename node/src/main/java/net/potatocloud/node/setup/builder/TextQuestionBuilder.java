package net.potatocloud.node.setup.builder;

import net.potatocloud.node.setup.Question;
import net.potatocloud.node.setup.Setup;
import net.potatocloud.node.setup.questions.TextQuestion;

public class TextQuestionBuilder extends BaseQuestionBuilder {

    public TextQuestionBuilder(Setup parent, String name, String prompt) {
        super(parent, name, prompt);
    }

    @Override
    public Question question() {
        return new TextQuestion(name, prompt);
    }
}
