package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.QuizResult;
import ru.otus.spring.domain.Student;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
    private final ScanStudentService scanStudentService;
    private final QuizEngineService quizEngineService;
    private final PrintQuizResultService printQuizResultService;
    private final ScanContinuationService scanContinuationService;

    @Override
    public void start() {
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
