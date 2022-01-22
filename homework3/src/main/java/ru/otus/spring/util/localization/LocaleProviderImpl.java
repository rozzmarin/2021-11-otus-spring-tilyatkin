package ru.otus.spring.util.localization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class LocaleProviderImpl implements LocaleProvider {
    private static final String langCountryLocalePattern = "^%s_([a-z]{2,3})_([A-Z]{2})\\.csv$";
    private static final String langLocalePattern = "^%s_([a-z]{2,3})_([A-Z]{2})\\.csv$";
    private static final String emptyLocalePattern = "^%s\\.csv$";
    private final String csvLocation;
    private final ResourcePatternResolver resourcePatternResolver;
    private Locale locale = Locale.getDefault();

    public LocaleProviderImpl(@Value("${quest.location}") String csvLocation, ResourcePatternResolver resourcePatternResolver) {
        this.csvLocation = csvLocation;
        this.resourcePatternResolver = resourcePatternResolver;
    }

    @Override
    public List<Locale> getLocales() {
        List<Locale> localeList = new ArrayList<>();
        try {
            for (Resource resource: resourcePatternResolver.getResources(this.csvLocation + "/" + this.csvLocation + "*.csv")) {
                String filename = resource.getFilename();
                Locale locale = getLangCountryLocale(filename);
                if (locale != null) {
                    localeList.add(locale);
                    continue;
                }
                locale = getLangLocale(filename);
                if (locale != null) {
                    localeList.add(locale);
                    continue;
                }
                locale = getEmptyLocale(filename);
                if (locale != null) {
                    localeList.add(locale);
                    continue;
                }
            }
        }
        catch (IOException ex) {
            throw new LocaleProviderException("Problem with load resources", ex);
        }
        return localeList;
    }

    @Override
    public Locale getLocale() {
        return this.locale;
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    private Locale getLangCountryLocale(String filename) {
        Pattern pattern = Pattern.compile(String.format(langCountryLocalePattern, this.csvLocation));
        Matcher matcher = pattern.matcher(filename);
        if (matcher.find()) {
            return new Locale(matcher.group(1), matcher.group(2));
        }
        return null;
    }

    private Locale getLangLocale(String filename) {
        Pattern pattern = Pattern.compile(String.format(langLocalePattern, this.csvLocation));
        Matcher matcher = pattern.matcher(filename);
        if (matcher.find()) {
            return new Locale(matcher.group(1));
        }
        return null;
    }

    private Locale getEmptyLocale(String filename) {
        Pattern pattern = Pattern.compile(String.format(emptyLocalePattern, this.csvLocation));
        Matcher matcher = pattern.matcher(filename);
        if (matcher.find()) {
            return Locale.forLanguageTag("und");
        }
        return null;
    }
}
