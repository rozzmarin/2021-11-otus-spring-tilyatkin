package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Student;

import java.util.Scanner;

@Service
public class ScanStudentServiceImpl implements ScanStudentService {
    @Override
    public Student scan() {
        Scanner scanner = new Scanner(System.in);
        String input;
        do {
            System.out.print("Enter your name, please: ");
            input = scanner.nextLine();
        }
        while (input.trim().equals(""));
        String studentName = input.trim();
        return new Student(studentName);
    }
}
