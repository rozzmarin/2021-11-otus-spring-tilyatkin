package ru.otus.spring.dao;

import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Question;
import ru.otus.spring.util.csv.CSVReader;
import ru.otus.spring.util.localization.LocaleProvider;

import java.util.List;

@Component
public class QuestionDaoCSV extends AbstractDaoCSV implements QuestionDao {
    public QuestionDaoCSV(CSVReader csvReader, LocaleProvider localeProvider) {
        super(csvReader, localeProvider);
    }

    @Override
    public int getCount() {
        return getRowList().size();
    }

    @Override
    public Question getByIndex(int index) {
        List<String> questionRow = getRowList().get(index);
        String questionBody = questionRow.get(0);
        if (questionRow.size() > 2) {
            List<String> answerOptions = questionRow.subList(2, questionRow.size());
            return new Question(questionBody, answerOptions);
        }
        return new Question(questionBody);
    }
}
