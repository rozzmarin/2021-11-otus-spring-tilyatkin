package ru.otus.spring.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.util.csv.CSVReader;
import ru.otus.spring.util.localization.LocaleProvider;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class AnswerDaoCSVTest {
    @MockBean
    private CSVReader csvReader;
    @MockBean
    private LocaleProvider localeProvider;
    @Autowired
    private AnswerDao answerDao;

    @ComponentScan("ru.otus.spring.dao")
    @Configuration
    static class NestedConfiguration {
    }

    @BeforeEach
    void setup() {
        BDDMockito.given(localeProvider.getLocale())
                .willReturn(Locale.US);
        BDDMockito.given(csvReader.readAll(Locale.US))
                .willReturn(Arrays.asList(
                        Arrays.asList("Question 1", "Answer 1"),
                        Arrays.asList("Question 2", "Answer 2"),
                        Arrays.asList("Question 3", "Answer 3"),
                        Arrays.asList("Question 4", "Answer 4"),
                        Arrays.asList("Question 5", "Answer 5")
                ));
    }

    @Test
    void getCount() {
        Assertions.assertThat(answerDao.getCount())
                .isEqualTo(5);
    }

    @Test
    void getByIndex() {
        Assertions.assertThat(answerDao.getByIndex(1))
                .as("Answer is not null").isNotNull()
                .as("Answer has correct body").matches(q -> Objects.equals(q.getBody(), "Answer 2"));
    }

    @Test
    void getByIndexOutOfRange() {
        Assertions.assertThatThrownBy(() -> {
            answerDao.getByIndex(6);
        }).isInstanceOf(IndexOutOfBoundsException.class);
    }
}
