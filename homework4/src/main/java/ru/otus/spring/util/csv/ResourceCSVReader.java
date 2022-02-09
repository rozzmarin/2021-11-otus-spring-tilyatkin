package ru.otus.spring.util.csv;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.util.*;

@Component
public class ResourceCSVReader implements CSVReader {
    private final String csvLocation;
    private final Map<Locale, URL> localeResourceMapMap = new HashMap<>();

    public ResourceCSVReader(@Value("${quest.location}") String csvLocation) {
        this.csvLocation = csvLocation;
    }

    @Override
    public List<List<String>> readAll(Locale locale) {
        URL resource = this.getResourceForLocale(locale);
        if (resource == null) {
            throw new MissingResourceException("Can't find csv", "", locale.getDisplayName());
        }

        try (InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream(), "UTF-8");
             com.opencsv.CSVReader openCSVReader = new com.opencsv.CSVReader(inputStreamReader)) {
            List result = new ArrayList<List<String>>();
            List<String[]> allRows = openCSVReader.readAll();
            for (String[] row : allRows) {
                result.add(Arrays.asList(row));
            }
            return result;
        }
        catch (Exception ex) {
            throw new CSVReaderException("Problem with reading csv", ex);
        }
    }

    private URL getResourceForLocale(Locale locale) {
        URL resource = this.localeResourceMapMap.get(locale);
        if (resource == null) {
            List<String> keyList = List.of(
                    String.format("%1$s/%1$s_%2$s_%3$s.csv", this.csvLocation, locale.getLanguage(), locale.getCountry()),
                    String.format("%1$s/%1$s_%2$s.csv", this.csvLocation, locale.getLanguage()),
                    String.format("%1$s/%1$s.csv", this.csvLocation));
            for (String key : keyList) {
                resource = getClass().getClassLoader().getResource(key);
                if (resource != null) {
                    this.localeResourceMapMap.put(locale, resource);
                    break;
                }
            }
        }
        return resource;
    }
}
