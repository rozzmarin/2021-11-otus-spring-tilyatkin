package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Answer;

import java.util.List;
import java.util.Scanner;

@Service
public class ScanAnswerServiceImpl implements ScanAnswerService {
    @Override
    public Answer scan(List<String> answerOptions) {
        String input = answerOptions != null && answerOptions.size() > 0 ?
                getOptionAnswerInput(answerOptions) :
                getAnswerInput();
        return new Answer(input);
    }

    private String getOptionAnswerInput(List<String> answerOptions) {
        Scanner scanner = new Scanner(System.in);
        int inputInt;
        do {
            System.out.print("Enter number of answer option: ");
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
        Scanner scanner = new Scanner(System.in);
        String input;
        do {
            System.out.print("Enter answer: ");
            input = scanner.nextLine();
        }
        while (input.trim().equals(""));
        return input;
    }
}
