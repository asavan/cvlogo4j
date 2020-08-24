package ru.asavan.cvlogo;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by asavan on 23.05.2020.
 */
public class CalendarExtractor implements Consumer<String> {
    private final Map<String, Color> color_map;
    private final Calendar cal = new Calendar();

    public CalendarExtractor(Map<String, Color> color_map) {
        this.color_map = color_map;
    }

    public void extract(List<String> lines) {
        for (String line : lines) {
            accept(line);
        }
    }

    @Override
    public void accept(String inputLine) {
        Day d = Parser.parseOneLine(inputLine, color_map);
        if (d != null) {
            cal.add(d);
        }
    }

    public Calendar getCalendar() {
        cal.precalc();
        return cal;
    }

    public void adjustDay(String date) {
        Day d = cal.getDayByDate(Parser.getLocalDate(date));
        if (d != null) {
            d.adjustDay();
        }
    }
}
