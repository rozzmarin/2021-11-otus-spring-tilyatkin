package ru.otus.spring.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class AnswerTest {
    @Test
    void shouldHaveCorrectConstructor() {
        Answer answer = new Answer("Answer 1");

        Assertions.assertThat(answer.getBody())
                .isEqualTo("Answer 1");
    }
}
