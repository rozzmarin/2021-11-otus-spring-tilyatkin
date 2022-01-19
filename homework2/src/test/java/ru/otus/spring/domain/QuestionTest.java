package ru.otus.spring.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class QuestionTest {
    @Test
    void shouldHaveCorrectConstructorWithAnswerOptions() {
        Question question = new Question("Question 1", "Answer 1", "Answer 2");

        Assertions.assertThat(question.getBody())
                .isEqualTo("Question 1");
        Assertions.assertThat(question.hasAnswerOptions())
                .isEqualTo(true);
        Assertions.assertThat(question.getAnswerOptions())
                .hasSize(2)
                .containsOnly("Answer 1", "Answer 2");
    }

    @Test
    void shouldHaveCorrectConstructorWithoutAnswerOptions() {
        Question question = new Question("Question 1");

        Assertions.assertThat(question.getBody())
                .isEqualTo("Question 1");
        Assertions.assertThat(question.hasAnswerOptions())
                .isEqualTo(false);
        Assertions.assertThat(question.getAnswerOptions())
                .hasSize(0);
    }
}
