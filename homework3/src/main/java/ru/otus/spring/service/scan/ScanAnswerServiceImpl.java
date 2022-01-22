package ru.otus.spring.service.scan;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.util.localization.CustomMessageSource;

import java.util.List;
import java.util.Scanner;

@Service
public class ScanAnswerServiceImpl extends AbstractScanService implements ScanAnswerService {
    public ScanAnswerServiceImpl(CustomMessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public Answer scan(List<String> answerOptions) {
        String input = answerOptions != null && answerOptions.size() > 0 ?
                getOptionAnswerInput(answerOptions) :
                getAnswerInput();
        return new Answer(input);
    }

    private String getOptionAnswerInput(List<String> answerOptions) {
        Scanner scanner = new Scanner(this.getIn());
        int inputInt;
        do {
            this.getOut().println();
            this.getOut().printf("%s: ", this.getMessage("quiz.answer.label-with-options"));
            String inputStr = scanner.nextLine();
            try {
                inputInt = Integer.parseInt(inputStr);
            }
            catch (NumberFormatException ex) {
                inputInt = 0;
            }
        }
        while (inputInt <= 0 || inputInt > answerOptions.size());
        String input = answerOptions.get(inputInt - 1);
        return input;
    }

    private String getAnswerInput() {
        Scanner scanner = new Scanner(this.getIn());
        String input;
        do {
            this.getOut().println();
            this.getOut().printf("%s: ", this.getMessage("quiz.answer.label"));
            input = scanner.nextLine();
        }
        while (input.trim().equals(""));
        return input;
    }
}
