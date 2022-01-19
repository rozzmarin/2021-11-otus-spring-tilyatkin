package ru.otus.spring.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import ru.otus.spring.util.csv.CSVReader;

import java.util.Arrays;
import java.util.Objects;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AnswerDaoCSVTest {
    private AnswerDaoCSV answerDao;

    @BeforeAll
    void setup() {
        CSVReader csvReader = Mockito.mock(CSVReader.class);
        BDDMockito.given(csvReader.readAll())
                .willReturn(Arrays.asList(
                        Arrays.asList("Question 1", "Answer 1"),
                        Arrays.asList("Question 2", "Answer 2"),
                        Arrays.asList("Question 3", "Answer 3"),
                        Arrays.asList("Question 4", "Answer 4"),
                        Arrays.asList("Question 5", "Answer 5")
                ));
        answerDao = new AnswerDaoCSV(csvReader);
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
