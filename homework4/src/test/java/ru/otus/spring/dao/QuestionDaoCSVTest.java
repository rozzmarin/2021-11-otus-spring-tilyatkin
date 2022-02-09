package ru.otus.spring.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.util.csv.CSVReader;
import ru.otus.spring.util.localization.LocaleProvider;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class QuestionDaoCSVTest {
    @MockBean
    private CSVReader csvReader;
    @MockBean
    private LocaleProvider localeProvider;
    @Autowired
    private QuestionDaoCSV questionDao;

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
                        Arrays.asList("Question 1", "", "Option 1.1", "Option 1.2", "Option 1.3", "Option 1.4"),
                        Arrays.asList("Question 2", ""),
                        Arrays.asList("Question 3", "", "Option 3.1", "Option 3.2", "Option 3.3", "Option 3.4"),
                        Arrays.asList("Question 4", "", "Option 4.1", "Option 4.2", "Option 4.3", "Option 4.4"),
                        Arrays.asList("Question 5", "", "Option 5.1", "Option 5.2", "Option 5.3", "Option 5.4")
                ));
    }

    @Test
    void getCount() {
        Assertions.assertThat(questionDao.getCount())
                .isEqualTo(5);
    }

    @Test
    void getByIndexWithAnswerOptions() {
        Assertions.assertThat(questionDao.getByIndex(3))
                .as("Question is not null").isNotNull()
                .as("Question has correct body").matches(q -> Objects.equals(q.getBody(), "Question 4"))
                .as("Question has answer options").matches(q -> q.hasAnswerOptions())
                .as("Question has correct answer options length").matches(q -> q.getAnswerOptions().size() == 4)
                .as("Question has correct answer options content").matches(q -> {
                    List<String> answerOptions = q.getAnswerOptions();
                    return Objects.equals(answerOptions.get(0), "Option 4.1")
                            && Objects.equals(answerOptions.get(1), "Option 4.2")
                            && Objects.equals(answerOptions.get(2), "Option 4.3")
                            && Objects.equals(answerOptions.get(3), "Option 4.4");
                });
    }

    @Test
    void getByIndexWithoutAnswerOptions() {
        Assertions.assertThat(questionDao.getByIndex(1))
                .as("Question is not null").isNotNull()
                .as("Question has correct body").matches(q -> Objects.equals(q.getBody(), "Question 2"))
                .as("Question has not answer options").matches(q -> !q.hasAnswerOptions())
                .as("Question has zero answer options length").matches(q -> q.getAnswerOptions().size() == 0);
    }

    @Test
    void getByIndexOutOfRange() {
        Assertions.assertThatThrownBy(() -> {
            questionDao.getByIndex(6);
        }).isInstanceOf(IndexOutOfBoundsException.class);
    }
}
