package ru.asavan.cvlogo;

import ru.asavan.cvlogo.templater.Templater;
import ru.asavan.cvlogo.templater.TemplaterFactory;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asavan on 23.05.2020.
 */
public class Commiter {
    public static final String GIT_URL = "git@github.com";
    private static final int DAYS_IN_WEEK = 7;
    private static final boolean DEBUG_PRINTING = false;

    public static String fake_it(Integer[][] image, Calendar cal, String username, String repo, int offset, OsName osName, boolean isNew, LocalDate githubErrorSince) {
        Templater templater = TemplaterFactory.chooseTemplater(osName);
        List<String> strings = generateValuesInDateOrder(image, cal, offset, templater, githubErrorSince);
        if (strings.isEmpty()) {
            return "";
        }
        return MessageFormat.format(templater.getMainTemplate(isNew), repo, String.join("\n", strings), GIT_URL, username);
    }

    private static List<String> generateValuesInDateOrder(Integer[][] image, Calendar cal, int offset, Templater templater, LocalDate githubErrorSince) {
        List<String> strings = new ArrayList<>();
        int days = offset * DAYS_IN_WEEK;
        if (cal.getFillColor() != null) {
            strings.addAll(prefill(days, cal, cal.getFillColor(), templater));
        }
        if (DEBUG_PRINTING) {
            System.out.println("Calendar first day " + cal.getDay(0).getDate());
            System.out.println("Image first day " + cal.getDay(days).getDate());
        }
        if (image != null) {
            days = fillImage(image, cal, templater, strings, days, githubErrorSince);
        }

        if (cal.getFillColor() != null) {
            strings.addAll(postfill(days, cal, cal.getFillColor(), templater));
        }
        return strings;
    }

    private static int fillImage(Integer[][] image, Calendar cal, Templater templater, List<String> strings, int days, LocalDate githubErrorSince) {
        int width = image[0].length;
        if (image.length > Commiter.DAYS_IN_WEEK) {
            System.out.println("Bad image sizing");
        }
        for (int w = 0; w < width; ++w) {
            for (int h = 0; h < Math.min(Commiter.DAYS_IN_WEEK, image.length); ++h) {
                Day d = cal.getDay(days);
                if (d == null) {
                    ++days;
                    continue;
                }
                Color neededColor = Pictures.getColor(image[h][w]);
                strings.addAll(solveOneDay(cal, neededColor, d, templater, true, githubErrorSince));
                ++days;
            }
        }
        return days;
    }

    private static List<String> solveOneDay(Calendar cal, Color neededColor, Day d, Templater templater, boolean shouldWarn, LocalDate githubErrorSince) {
        int minCount = cal.getMinCount(neededColor);
        Integer maxCount = cal.getMaxCount(neededColor);
        List<String> strings = new ArrayList<>();
        int count = minCount - d.getCount();
        if (count < 0) {
            if (shouldWarn && (maxCount == null || maxCount < d.getCount())) {
                Integer maxCountNext = cal.getMaxCount(Pictures.nextColor(neededColor));
                if (maxCountNext != null && maxCountNext < d.getCount()) {
                    System.out.println("Very Bad pixel at day " + d.getDate() + " " + d.getCount() + " " + minCount + " " + maxCount + " " + neededColor);
                } else {
                    System.out.println("Bad pixel at day " + d.getDate() + " " + d.getCount() + " " + minCount + " " + maxCount + " " + neededColor);
                }
            }
            return strings;
        }
        // TODO github double count commit after this date. Remove after bug fixed
        if (githubErrorSince != null && d.getDate().isAfter(githubErrorSince)) {
            if (count > 1) {
                count = (count + 1) / 2;
            }
            // return;
        }

        for (int j = 0; j < count; ++j) {
            String c = commit(d.getTime(j + d.getCount()), templater, j + 1 + d.getCount());
            strings.add(c);
        }
        return strings;
    }

    private static List<String> fillOneColor(int begin, int end, Calendar cal, Color neededColor, Templater templater) {
        List<String> strings = new ArrayList<>();
        for (int i = begin; i < end; ++i) {
            strings.addAll(solveOneDay(cal, neededColor, cal.getDay(i), templater, false, null));
        }
        return strings;
    }

    private static List<String> prefill(int days, Calendar cal, Color neededColor, Templater templater) {
        return fillOneColor(0, days, cal, neededColor, templater);
    }

    private static List<String> postfill(int days, Calendar cal, Color neededColor, Templater templater) {
        return fillOneColor(days, cal.size(), cal, neededColor, templater);
    }

    private static String commit(LocalDateTime date, Templater templater, int count) {
        return MessageFormat.format(templater.getCommitTemplate(), date.format(DateTimeFormatter.ISO_DATE_TIME), count);
    }
}
