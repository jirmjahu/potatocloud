package net.potatocloud.node.setup.builder;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.potatocloud.node.setup.Question;
import net.potatocloud.node.setup.QuestionSkipCondition;
import net.potatocloud.node.setup.Setup;
import net.potatocloud.node.setup.SuggestionProvider;
import net.potatocloud.node.setup.validator.AnswerValidator;

@RequiredArgsConstructor
@Setter
@Accessors(fluent = true, chain = true)
public abstract class BaseQuestionBuilder {

    protected final Setup parent;
    protected final String name;
    protected final String prompt;

    protected String defaultAnswer;
    protected SuggestionProvider suggestions;
    protected QuestionSkipCondition skipIf;
    protected AnswerValidator customValidator;

    public abstract Question question();

    public void add() {
        final Question question = question();

        if (defaultAnswer != null) {
            question.setDefaultAnswer(defaultAnswer);
        }

        if (suggestions != null) {
            question.setSuggestions(suggestions);
        }

        if (skipIf != null) {
            question.setSkipCondition(skipIf);
        }

        if (customValidator != null) {
            question.setCustomValidator(customValidator);
        }

        parent.getQuestions().add(question);
    }
}
