package ru.asavan.cvlogo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by asavan on 27.02.2022.
 */
public class GitHub {
    private static final String GITHUB_BASE_URL = "https://github.com/";

    private GitHub() {
    }

    public static String createRepo(String username, String repo) {
        return String.format("Create a new(!) repo named %s at %s%s?tab=repositories and run the script",
                repo, GITHUB_BASE_URL, username);
    }

    public static CalendarExtractor getCalendarExtractor(Consumer<Predicate<String>> consumer) {
        CalendarExtractor calendarExtractor = new CalendarExtractor();
        consumer.accept(calendarExtractor);
        return calendarExtractor;
    }

    public static void retrieveContributionsCalendar(Predicate<String> pred, String username, boolean debugPrint) {
        String base_url = GITHUB_BASE_URL + "users/" + username;
        String url = base_url + "/contributions";
        if (debugPrint) {
            System.out.println(url);
        }
        try {
            URL u = new URL(url);

            try (InputStream in = u.openStream()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    if (pred.test(inputLine)) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
