package ru.otus.spring.domain;

import java.util.Collections;
import java.util.List;

public class Question {
    private final String body;
    private final List<String> answerOptions;

    public Question(String body, String... answerOptions) {
        this.body = body;
        this.answerOptions = List.of(answerOptions);
    }

    public Question(String body, List<String> answerOptions) {
        this.body = body;
        this.answerOptions = Collections.unmodifiableList(answerOptions);
    }

    public String getBody() {
        return this.body;
    }

    public List<String> getAnswerOptions() {
        return this.answerOptions;
    }

    public boolean hasAnswerOptions() {
        return this.answerOptions != null && this.answerOptions.size() > 0;
    }
}
