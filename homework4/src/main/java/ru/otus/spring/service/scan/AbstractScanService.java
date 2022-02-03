package ru.otus.spring.service.scan;

import ru.otus.spring.service.print.AbstractPrintService;
import ru.otus.spring.util.localization.CustomMessageSource;

import java.io.InputStream;

public abstract class AbstractScanService extends AbstractPrintService {
    public AbstractScanService(CustomMessageSource messageSource) {
        super(messageSource);
    }

    protected InputStream getIn() {
        return System.in;
    }
}
