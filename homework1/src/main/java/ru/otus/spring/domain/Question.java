package ru.otus.spring.domain;

public class Question {

    private final String body;
    private final String[] answerOptions;

    public Question(String body, String... answerOptions) {
        this.body = body;
        this.answerOptions = answerOptions;
    }

    public String getBody() {
        return this.body;
    }

    public String[] getAnswerOptions() {
        return this.answerOptions;
    }

    public boolean hasAnswerOptions() {
        return this.answerOptions != null && this.answerOptions.length > 0;
    }
}
