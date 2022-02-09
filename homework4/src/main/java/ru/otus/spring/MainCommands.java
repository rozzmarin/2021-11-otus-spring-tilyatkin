package ru.otus.spring;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.spring.domain.QuizResult;
import ru.otus.spring.domain.Student;
import ru.otus.spring.service.QuizEngineService;
import ru.otus.spring.service.print.PrintLocaleService;
import ru.otus.spring.service.print.PrintQuizResultService;
import ru.otus.spring.service.scan.ScanLocaleService;
import ru.otus.spring.service.scan.ScanStudentService;
import ru.otus.spring.util.localization.CustomMessageSource;
import ru.otus.spring.util.localization.LocaleProvider;

import java.util.List;
import java.util.Locale;

@ShellComponent
@RequiredArgsConstructor
public class MainCommands {
    private final LocaleProvider localeProvider;
    private final PrintLocaleService printLocaleService;
    private final ScanLocaleService scanLocaleService;
    private final ScanStudentService scanStudentService;
    private final QuizEngineService quizEngineService;
    private final PrintQuizResultService printQuizResultService;
    private final CustomMessageSource customMessageSource;

    private Student student;
    private QuizResult quizResult;

    @ShellMethod(value = "Select locale", key = {"l", "locale"})
    public void locale() {
        List<Locale> localeList = localeProvider.getLocales();
        this.printLocaleService.print(localeList);
        Locale locale = this.scanLocaleService.scan(localeList);
        localeProvider.setLocale(locale);
    }

    @ShellMethod(value = "Enter name", key = {"n", "name"})
    public void name() {
        student = scanStudentService.scan();
    }

    @ShellMethod(value = "Start quiz", key = {"q", "quiz"})
    @ShellMethodAvailability(value = "isQuizCommandAvailable")
    public void quiz() {
        quizResult = quizEngineService.process();
    }

    @ShellMethod(value = "Print result", key = {"r", "result"})
    @ShellMethodAvailability(value = "isResultCommandAvailable")
    public void result() {
        printQuizResultService.print(student, quizResult);
    }

    private Availability isQuizCommandAvailable() {
        return student == null ?
                Availability.unavailable(customMessageSource.getMessage("quiz.unavailable")) :
                Availability.available();
    }

    private Availability isResultCommandAvailable() {
        return quizResult == null ?
                Availability.unavailable(customMessageSource.getMessage("quiz.result.unavailable")) :
                Availability.available();
    }
}
