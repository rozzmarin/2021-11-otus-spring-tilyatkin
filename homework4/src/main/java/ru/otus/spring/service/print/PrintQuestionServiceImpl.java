package ru.otus.spring.service.print;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Question;
import ru.otus.spring.util.localization.CustomMessageSource;

import java.util.List;

@Service
public class PrintQuestionServiceImpl extends AbstractPrintService implements PrintQuestionService {
    public PrintQuestionServiceImpl(CustomMessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public void print(Question question) {
        this.getOut().println();
        this.getOut().printf("%s: %s", this.getMessage("quiz.question.label"), question.getBody());
        if (question.hasAnswerOptions()) {
            List<String> answerOptions = question.getAnswerOptions();
            for (int i = 0; i < answerOptions.size(); i++) {
                this.getOut().println();
                this.getOut().printf("%d. %s", i + 1, answerOptions.get(i));
            }
        }
    }
}
