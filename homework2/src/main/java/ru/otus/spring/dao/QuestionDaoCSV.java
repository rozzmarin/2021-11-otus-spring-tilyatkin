package ru.otus.spring.dao;

import ru.otus.spring.domain.Question;
import ru.otus.spring.util.csv.CSVReader;

import java.util.List;

public class QuestionDaoCSV implements QuestionDao {
    private final CSVReader csvReader;
    private List<List<String>> questionList;

    public QuestionDaoCSV(CSVReader csvReader) {
        this.csvReader = csvReader;
    }

    private List<List<String>> getQuestionList() {
        if (questionList == null) {
            questionList = csvReader.readAll();
        }
        return questionList;
    }

    @Override
    public int getCount() {
        return getQuestionList().size();
    }

    @Override
    public Question getByIndex(int index) {
        List<String> questionRow = getQuestionList().get(index);
        String questionBody = questionRow.get(0);
        if (questionRow.size() > 2) {
            List<String> answerOptions = questionRow.subList(2, questionRow.size());
            return new Question(questionBody, answerOptions);
        }
        return new Question(questionBody);
    }
}
