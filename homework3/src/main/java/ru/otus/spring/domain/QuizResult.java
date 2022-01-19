package ru.otus.spring.domain;

public class QuizResult {
    private int answersCount;
    private int correctAnswersCount;

    public QuizResult() {
        this(0, 0);
    }

    public QuizResult(int answersCount, int correctAnswersCount) {
        if (answersCount < 0) {
            throw new IllegalArgumentException("Argument answersCount must be greater than zero");
        }
        if (correctAnswersCount < 0) {
            throw new IllegalArgumentException("Argument correctAnswersCount must be greater than or equal zero");
        }
        if (correctAnswersCount > answersCount) {
            throw new IllegalArgumentException("Argument correctAnswersCount must be less than or equal answersCount");
        }

        this.answersCount = answersCount;
        this.correctAnswersCount = correctAnswersCount;
    }

    public int getAnswersCount() {
        return this.answersCount;
    }

    public int getCorrectAnswersCount() {
        return this.correctAnswersCount;
    }

    public void addAnswer(boolean isCorrectAnswer) {
        this.answersCount++;
        if (isCorrectAnswer) {
            this.correctAnswersCount++;
        }
    }
}
