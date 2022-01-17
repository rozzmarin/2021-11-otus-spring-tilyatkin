package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.QuizResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizEngineServiceImpl implements QuizEngineService  {
    private final QuestionService questionService;
    private final PrintQuestionService printQuestionService;
    private final AnswerService answerService;
    private final ScanAnswerService scanAnswerService;

    @Override
    public QuizResult process() {
        QuizResult quizResult = new QuizResult();
        List<Question> questions = this.questionService.getAllQuestions();
        for (int index = 0; index < questions.size(); index++) {
            Question question = questions.get(index);
            this.printQuestionService.print(question);
            Answer answer = this.scanAnswerService.scan(question.getAnswerOptions());
            boolean isCorrectAnswer = this.answerService.checkAnswer(index, answer);
            quizResult.addAnswer(isCorrectAnswer);
        }
        return quizResult;
    }

    @Override
    public boolean canContinue(QuizResult quizResult) {
        return quizResult.getCorrectAnswersCount() < quizResult.getAnswersCount();
    }
}
