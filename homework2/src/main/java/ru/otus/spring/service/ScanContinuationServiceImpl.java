package ru.otus.spring.service;

import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class ScanContinuationServiceImpl implements ScanContinuationService {
    @Override
    public boolean scan() {
        System.out.println();
        System.out.println("Want to try again? [Y]: ");
        Scanner scanner = new Scanner(System.in);
        String check = scanner.nextLine();
        return check.equalsIgnoreCase("Y");
    }
}
