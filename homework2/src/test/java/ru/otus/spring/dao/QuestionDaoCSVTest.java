package ru.otus.spring.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import ru.otus.spring.util.csv.CSVReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QuestionDaoCSVTest {
    private QuestionDaoCSV questionDao;

    @BeforeAll
    void setup() throws IOException {
        CSVReader csvReader = Mockito.mock(CSVReader.class);
        BDDMockito.given(csvReader.readAll())
                .willReturn(Arrays.asList(
                        new String[] { "Question 1", "", "Option 1.1", "Option 1.2", "Option 1.3", "Option 1.4" },
                        new String[] { "Question 2", "" },
                        new String[] { "Question 3", "", "Option 3.1", "Option 3.2", "Option 3.3", "Option 3.4" },
                        new String[] { "Question 4", "", "Option 4.1", "Option 4.2", "Option 4.3", "Option 4.4" },
                        new String[] { "Question 5", "", "Option 5.1", "Option 5.2", "Option 5.3", "Option 5.4" }
                ));
        questionDao = new QuestionDaoCSV(csvReader);
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
                .as("Question has correct answer options length").matches(q -> q.getAnswerOptions().length == 4)
                .as("Question has correct answer options content").matches(q -> {
                    String[] answerOptions = q.getAnswerOptions();
                    return Objects.equals(answerOptions[0], "Option 4.1")
                            && Objects.equals(answerOptions[1], "Option 4.2")
                            && Objects.equals(answerOptions[2], "Option 4.3")
                            && Objects.equals(answerOptions[3], "Option 4.4");
                });
    }

    @Test
    void getByIndexWithoutAnswerOptions() {
        Assertions.assertThat(questionDao.getByIndex(1))
                .as("Question is not null").isNotNull()
                .as("Question has correct body").matches(q -> Objects.equals(q.getBody(), "Question 2"))
                .as("Question has not answer options").matches(q -> !q.hasAnswerOptions())
                .as("Question has zero answer options length").matches(q -> q.getAnswerOptions().length == 0);
    }

    @Test
    void getByIndexOutOfRange() {
        Assertions.assertThatThrownBy(() -> {
            questionDao.getByIndex(6);
        }).isInstanceOf(IndexOutOfBoundsException.class);
    }
}
