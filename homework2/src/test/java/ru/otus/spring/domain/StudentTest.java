package ru.otus.spring.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class StudentTest {
    @Test
    void shouldHaveCorrectConstructor() {
        Student student = new Student("Ivan Ivanov");

        Assertions.assertThat(student.getName())
                .isEqualTo("Ivan Ivanov");
    }
}
