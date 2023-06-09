package ru.asavan.cvlogo;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by asavan on 24.05.2020.
 */
class SvgExtractorTest {

    @Test
    void testSvg() {
        MemoryCollector m = new MemoryCollector();
        simulateCalendar(new SvgExtractor(m));
        List<String> ans = m.getArr();
        System.out.println(String.join("\n", ans));
    }

    @Test
    void testColorParse2023() {
        parseYear("calendar2023.txt");
    }

    @Test
    void testCalendar() {
        Calendar cal = GitHub.getCalendarExtractor(pred ->
                GitHub.retrieveContributionsCalendar(pred, "asavan", true)).getCalendar();
        assertTrue(cal.size() >= 52*7);
    }

    private static void parseYear(String name) {
        CalendarExtractor calendarExtractor = GitHub.getCalendarExtractor(pred -> readCalendarFromFile(pred, name));
        Calendar calendar = calendarExtractor.getCalendar();
        System.out.println(calendar);
        System.out.println(calendar.minCountPrintable());
    }

    private void simulateCalendar(Predicate<String> s) {
        readCalendarFromFile(s, "calendar2023.txt");
    }

    private static void readCalendarFromFile(Predicate<String> pred, final String name) {
        File file = new File(SvgExtractorTest.class.getResource("/" + name).getFile());
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                if (pred.test(inputLine)) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
