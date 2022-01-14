package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;

import java.util.List;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
    private final QuestionService questionService;
    private final PrintQuestionService printQuestionService;
    private final AnswerService answerService;
    private final ScanAnswerService scanAnswerService;

    @Override
    public void start() {
        QuizAssistant assistant = new QuizAssistant();
        assistant.doStart();
        do {
            assistant.doProcess();
        }
        while (!assistant.doFinish());
    }

    private class QuizAssistant {
        private int answersCount = 0;
        private int correctAnswersCount = 0;
        private String studentName;

        private void reset() {
            answersCount = 0;
            correctAnswersCount = 0;
        }

        private void applyAnswer(boolean isCorrectAnswer) {
            answersCount++;
            if (isCorrectAnswer) {
                correctAnswersCount++;
            }
        }

        public void doStart() {
            Scanner scanner = new Scanner(System.in);
            String input;
            do {
                System.out.print("Enter your name, please: ");
                input = scanner.nextLine();
            }
            while (input.trim().equals(""));
            studentName = input.trim();
        }

        public boolean doFinish() {
            System.out.println();
            System.out.printf("%s, your result is %d correct answers of %d", studentName, correctAnswersCount, answersCount);
            if (correctAnswersCount < answersCount) {
                System.out.println();
                System.out.println("Want to try again? [Y]: ");
                Scanner scanner = new Scanner(System.in);
                String check = scanner.nextLine();
                if (check.equalsIgnoreCase("Y")) {
                    return false;
                }
            }
            return true;
        }

        public void doProcess() {
            reset();
            QuizServiceImpl outer = QuizServiceImpl.this;
            List<Question> questions = outer.questionService.getAllQuestions();
            for (int index = 0; index < questions.size(); index++) {
                Question question = questions.get(index);
                outer.printQuestionService.print(question);
                Answer answer = outer.scanAnswerService.scan(question.getAnswerOptions());
                boolean checkResult = outer.answerService.checkAnswer(index, answer);
                this.applyAnswer(checkResult);
            }
        }
    }
}
