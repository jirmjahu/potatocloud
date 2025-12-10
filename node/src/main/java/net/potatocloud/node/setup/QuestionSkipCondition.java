package net.potatocloud.node.setup;

import java.util.Map;

public interface QuestionSkipCondition {

    boolean skip(Map<String, String> answers);

}
