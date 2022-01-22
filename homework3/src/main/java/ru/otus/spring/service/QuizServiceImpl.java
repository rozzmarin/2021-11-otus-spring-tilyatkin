package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.QuizResult;
import ru.otus.spring.domain.Student;
import ru.otus.spring.service.print.PrintLocaleService;
import ru.otus.spring.service.print.PrintQuizResultService;
import ru.otus.spring.service.scan.ScanContinuationService;
import ru.otus.spring.service.scan.ScanLocaleService;
import ru.otus.spring.service.scan.ScanStudentService;
import ru.otus.spring.util.localization.LocaleProvider;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
    private final LocaleProvider localeProvider;
    private final PrintLocaleService printLocaleService;
    private final ScanLocaleService scanLocaleService;
    private final ScanStudentService scanStudentService;
    private final QuizEngineService quizEngineService;
    private final PrintQuizResultService printQuizResultService;
    private final ScanContinuationService scanContinuationService;

    @Override
    public void start() {
        List<Locale> localeList = localeProvider.getLocales();
        this.printLocaleService.print(localeList);
        Locale locale = this.scanLocaleService.scan(localeList);
        localeProvider.setLocale(locale);
        boolean canContinue;
        Student student = this.scanStudentService.scan();
        do {
            QuizResult quizResult = this.quizEngineService.process();
            this.printQuizResultService.print(student, quizResult);
            canContinue = this.quizEngineService.canContinue(quizResult) && this.scanContinuationService.scan();
        }
        while (canContinue);
    }
}
