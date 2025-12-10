package net.potatocloud.node.setup.builder;

import lombok.RequiredArgsConstructor;
import net.potatocloud.node.setup.Setup;

@RequiredArgsConstructor
public class QuestionBuilder {

    private final Setup parent;
    private final String name;

    public TextQuestionBuilder text(String prompt) {
        return new TextQuestionBuilder(parent, name, prompt);
    }

    public BooleanQuestionBuilder bool(String prompt) {
        return new BooleanQuestionBuilder(parent, name, prompt);
    }

    public NumberQuestionBuilder number(String prompt) {
        return new NumberQuestionBuilder(parent, name, prompt);
    }
}
