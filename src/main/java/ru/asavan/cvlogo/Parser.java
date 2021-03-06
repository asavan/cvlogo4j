package ru.asavan.cvlogo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by asavan on 23.05.2020.
 */
public class Parser {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDate getLocalDate(String date) {
        return LocalDate.parse(date, FORMATTER);
    }

    private static int getCount(String count) {
        return Integer.parseInt(count);
    }

    public static Day parseOneLine(String line, ColorExtractor colorExtractor) {
        String fill = getValue(line, "data-level");
        if (fill == null) {
            return null;
        }
        Color color = colorExtractor.getColor(fill);
        int count = getCount(getValue(line, "data-count"));
        String dataStr = getValue(line, "data-date");
        LocalDate date = getLocalDate(dataStr);
        return new Day(color, count, date);
    }

    private static String getValue(String line, final String param) {
        int index = line.indexOf(param + "=\"");
        if (index < 0) {
            return null;
        }
        int endIndex = line.indexOf("\"", index + param.length() + 2);
        return line.substring(index + param.length() + 2, endIndex);
    }
}
