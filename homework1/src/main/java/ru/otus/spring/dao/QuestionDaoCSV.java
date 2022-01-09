package ru.otus.spring.dao;

import ru.otus.spring.domain.Question;
import ru.otus.spring.util.csv.CSVReader;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class QuestionDaoCSV implements QuestionDao {
    private final List<String[]> questionList;

    public QuestionDaoCSV(CSVReader csvReader) throws IOException {
        questionList = csvReader.readAll();
    }

    public int getCount() {
        return questionList.size();
    }

    public Question getByIndex(int index) {
        String[] questionRow = questionList.get(index);
        String questionBody = questionRow[0];
        String[] answerOptions = new String[0];
        if (questionRow.length > 2) {
            answerOptions = Arrays.copyOfRange(questionRow, 2, questionRow.length);
        }
        return new Question(questionBody, answerOptions);
    }
}
