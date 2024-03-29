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

    private static int getCount(String line) {
        String toFind = "class=\"sr-only position-absolute\">";
        int begin = line.indexOf(toFind) + toFind.length();
        int end = line.indexOf(" contribution");
        if (begin < 0 || end < 0) return 0;
        String data = line.substring(begin, end);
        if ("No".equals(data)) {
            return 0;
        }
        return Integer.parseInt(data);
    }

    private static Color getColor(String c) {
        return Color.values()[c.charAt(0) - '0'];
    }

    public static int parseNextLine(String line) {
        return getCount(line);
    }

    public static Day parseOneLine(String line) {
        String dataLevel = getValue(line, "data-level");
        if (dataLevel == null || dataLevel.isEmpty()) {
            return null;
        }
        String dataStr = getValue(line, "data-date");
        if (dataStr == null || dataStr.isEmpty()) {
            return null;
        }
        Color color = getColor(dataLevel);
        int count = getCount(line);
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
