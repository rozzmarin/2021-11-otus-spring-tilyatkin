package ru.otus.spring.service.print;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.QuizResult;
import ru.otus.spring.domain.Student;
import ru.otus.spring.util.localization.CustomMessageSource;

@Service
public class PrintQuizResultServiceImpl extends AbstractPrintService implements PrintQuizResultService {
    public PrintQuizResultServiceImpl(CustomMessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public void print(Student student, QuizResult quizResult) {
        this.getOut().println();
        this.getOut().print(this.getMessage("quiz.result"
                , student.getName()
                , quizResult.getCorrectAnswersCount()
                , quizResult.getAnswersCount()));
    }
}
