package ru.asavan.cvlogo;

import java.util.function.Predicate;

/**
 * Created by asavan on 23.05.2020.
 */
public class CalendarExtractor implements Predicate<String> {
    private final Calendar cal = new Calendar();

    @Override
    public boolean test(String inputLine) {
        Day d = Parser.parseOneLine(inputLine);
        if (d != null) {
            cal.add(d);
        }
        return false;
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
