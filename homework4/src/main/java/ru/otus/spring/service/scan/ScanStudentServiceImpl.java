package ru.otus.spring.service.scan;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Student;
import ru.otus.spring.util.localization.CustomMessageSource;

import java.util.Scanner;

@Service
public class ScanStudentServiceImpl extends AbstractScanService implements ScanStudentService {
    public ScanStudentServiceImpl(CustomMessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public Student scan() {
        Scanner scanner = new Scanner(this.getIn());
        String input;
        do {
            this.getOut().println();
            this.getOut().printf("%s: ", this.getMessage("quiz.student.name"));
            input = scanner.nextLine();
        }
        while (input.trim().equals(""));
        String studentName = input.trim();
        return new Student(studentName);
    }
}
