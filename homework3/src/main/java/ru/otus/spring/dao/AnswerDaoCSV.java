package ru.otus.spring.dao;

import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.util.csv.CSVReader;
import ru.otus.spring.util.localization.LocaleProvider;

import java.util.List;

@Repository
public class AnswerDaoCSV extends AbstractDaoCSV implements AnswerDao {
    public AnswerDaoCSV(CSVReader csvReader, LocaleProvider localeProvider) {
        super(csvReader, localeProvider);
    }

    @Override
    public int getCount() {
        return getRowList().size();
    }

    @Override
    public Answer getByIndex(int index) {
        List<String> answerRow = getRowList().get(index);
        String answerBody = answerRow.get(1);
        return new Answer(answerBody);
    }
}
