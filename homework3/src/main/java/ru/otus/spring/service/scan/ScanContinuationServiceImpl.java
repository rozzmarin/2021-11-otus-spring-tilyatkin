package ru.otus.spring.service.scan;

import org.springframework.stereotype.Service;
import ru.otus.spring.util.localization.CustomMessageSource;

import java.util.Scanner;

@Service
public class ScanContinuationServiceImpl extends AbstractScanService implements ScanContinuationService {
    public ScanContinuationServiceImpl(CustomMessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public boolean scan() {
        this.getOut().println();
        this.getOut().printf("%s [Y]: ", this.getMessage("quiz.continue"));
        Scanner scanner = new Scanner(this.getIn());
        String check = scanner.nextLine();
        return check.equalsIgnoreCase("Y");
    }
}
