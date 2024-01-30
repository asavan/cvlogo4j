package ru.asavan.cvlogo;

import java.util.function.Predicate;

/**
 * Created by asavan on 23.05.2020.
 */
public class CalendarExtractor implements Predicate<String> {
    private final Calendar cal = new Calendar();
    private Day dTemp = null;

    @Override
    public boolean test(String inputLine) {
        if (dTemp != null) {
            int count = Parser.parseNextLine(inputLine);
            Day toStore = new Day(dTemp.getColor(), count, dTemp.getDate());
            cal.add(toStore);
            dTemp = null;
        }
        Day d = Parser.parseOneLine(inputLine);
        if (d != null) {
            dTemp = d;
            // cal.add(d);
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
