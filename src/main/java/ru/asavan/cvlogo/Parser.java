package ru.asavan.cvlogo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Created by asavan on 23.05.2020.
 */
public class Parser {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final Map<String, Color> COLOR_MAP = Map.of(
            "#ebedf0", Color.NONE,
            "#9be9a8", Color.ONE,
            "#40c463", Color.TWO,
            "#30a14e", Color.TREE,
            "#216e39", Color.FOUR
    );
    private static final String BACKGROUND_COLOR = "background-color: ";

    private static Color getColor(String color, Map<String, Color> colorMap) {
        Color c = colorMap.get(color);
        if (c == null) {
            throw new RuntimeException("Unknown color " + color);
        }
        return c;
    }

    public static LocalDate getLocalDate(String date) {
        return LocalDate.parse(date, FORMATTER);
    }

    private static int getCount(String count) {
        return Integer.parseInt(count);
    }

    public static String parseColorMap(String line) {
        return getBackgroundColor(line);
    }

    public static Day parseOneLine(String line, Map<String, Color> colorMap) {
        String fill = getValue(line, "fill");
        if (fill == null) {
            return null;
        }
        Color color = getColor(fill, colorMap);
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

    private static String getBackgroundColor(String line) {
        int index = line.indexOf(BACKGROUND_COLOR);
        if (index < 0) {
            return null;
        }
        int endIndex = line.indexOf("\"", index + BACKGROUND_COLOR.length());
        return line.substring(index + BACKGROUND_COLOR.length(), endIndex);
    }
}
