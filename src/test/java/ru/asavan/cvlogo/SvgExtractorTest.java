package ru.asavan.cvlogo;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by asavan on 24.05.2020.
 */
class SvgExtractorTest {

    @Test
    void testSvg() {
        MemoryCollector m = new MemoryCollector();
        SvgExtractor s = new SvgExtractor(m);
        simulateCalendar(s);
        List<String> ans = m.getArr();
        System.out.println(String.join("\n", ans));
    }

    @Test
    void testCalendar() {
        Map<String, Color> COLOR_MAP = Map.of(
                "#ebedf0", Color.NONE,
                "#c6e48b", Color.ONE,
                "#7bc96f", Color.TWO,
                "#239a3b", Color.TREE,
                "#196127", Color.FOUR
        );
        CalendarExtractor m = new CalendarExtractor(COLOR_MAP);
        SvgExtractor s = new SvgExtractor(m);
        simulateCalendar(s);
        Calendar calendar = m.getCalendar();
        System.out.println(calendar);
        System.out.println(calendar.minCountPrintable());
    }

    @Test
    void testColorParse() {
        parseYear("calendar2020.txt");
        parseYear("calendar2019.txt");
    }

    private void parseYear(String name) {
        CalendarExtractor calendarExtractor = Runner.getCalendarExtractor(pred -> readCalendarFromFile(pred, name));
        Calendar calendar = calendarExtractor.getCalendar();
        System.out.println(calendar);
        System.out.println(calendar.minCountPrintable());
    }

    private void simulateCalendar(Predicate<String> s) {
        readCalendarFromFile(s, "calendar2019.txt");
    }

    private void readCalendarFromFile(Predicate<String> pred, final String name) {
        ClassLoader classLoader = getClass().getClassLoader();

        File file = new File(classLoader.getResource(name).getFile());
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                if (pred.test(inputLine)) {
                    break;
                }
            }
            reader.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
