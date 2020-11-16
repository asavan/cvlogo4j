package ru.asavan.cvlogo;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
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

    public static String fake_it(Integer[][] image, Calendar cal, String username, String repo, int offset, OsName osName) {
        Templater templater = chooseTemplater(osName);
        List<String> strings = generateValuesInDateOrder(image, cal, offset, templater);
        if (strings.isEmpty()) {
            return "";
        }
        return MessageFormat.format(templater.getMainTemplate(), repo, String.join("\n", strings), GIT_URL, username);
    }

    public static String fill(Integer[][] image, Calendar cal, String repo, int offset) {
        Templater templater = new WinFillTemplater();
        List<String> strings = generateValuesInDateOrder(image, cal, offset, templater);
        if (strings.isEmpty()) {
            return "";
        }
        return MessageFormat.format(templater.getMainTemplate(), repo, String.join("\n", strings));
    }

    private static Templater chooseTemplater(OsName osName) {
        if (osName == OsName.WIN) {
            return new WinTemplater();
        }
        return new LinuxTemplater();
    }

    private static List<String> generateValuesInDateOrder(Integer[][] image, Calendar cal, int offset, Templater templater) {
        List<String> strings = new ArrayList<>();
        int days = offset * DAYS_IN_WEEK;
        if (cal.getFillColor() != null) {
            prefill(days, strings, cal, cal.getFillColor(), templater);
        }
        if (DEBUG_PRINTING) {
            System.out.println("Calendar first day " + cal.getDay(0).getDate());
            System.out.println("Image first day " + cal.getDay(days).getDate());
        }
        if (image != null) {
            days = fillImage(image, cal, templater, strings, days);
        }

        if (cal.getFillColor() != null) {
            postfill(days, strings, cal, cal.getFillColor(), templater);
        }
        return strings;
    }

    private static int fillImage(Integer[][] image, Calendar cal, Templater templater, List<String> strings, int days) {
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
                int minCount = cal.getMinCount(neededColor);
                solveOneDay(strings, cal, minCount, days, neededColor, true, templater);
                ++days;
            }
        }
        return days;
    }

    private static void solveOneDay(List<String> strings, Calendar cal, int minCount, int i, Color neededColor, boolean shouldWarn, Templater templater) {
        Day d = cal.getDay(i);
        // TODO github double count commit after this date. Remove after bug fixed
        if (d.getDate().isAfter(LocalDate.of(2020, Month.MAY, 12))) {
            return;
        }
        int count = minCount - d.getCount();
        if (count < 0) {
            Integer maxCount = cal.getMaxCount(neededColor);
            if (shouldWarn && (maxCount == null || maxCount < d.getCount())) {
                System.out.println("Bad pixel at day " + d.getDate() + " " + d.getCount() + " " + minCount + " " + maxCount + " " + neededColor);
            }
            return;
        }
        for (int j = 0; j < count; ++j) {
            String c = commit(d.getTime(j + d.getCount()), templater, j + 1);
            strings.add(c);
        }
    }

    private static void fillOneColor(int begin, int end, List<String> strings, Calendar cal, Color neededColor, Templater templater) {
        int minCount = cal.getMinCount(neededColor);
        for (int i = begin; i < end; ++i) {
            solveOneDay(strings, cal, minCount, i, neededColor, false, templater);
        }
    }

    private static void prefill(int days, List<String> strings, Calendar cal, Color neededColor, Templater templater) {
        fillOneColor(0, days, strings, cal, neededColor, templater);
    }

    private static void postfill(int days, List<String> strings, Calendar cal, Color neededColor, Templater templater) {
        fillOneColor(days, cal.size() - 1, strings, cal, neededColor, templater);
    }

    private static String commit(LocalDateTime date, Templater templater, int count) {
        return MessageFormat.format(templater.getCommitTemplate(), date.format(DateTimeFormatter.ISO_DATE_TIME), count);
    }
}
