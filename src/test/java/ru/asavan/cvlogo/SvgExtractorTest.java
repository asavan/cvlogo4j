package ru.asavan.cvlogo;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void testColorParse2021() {
        parseYear("calendar2021.txt");
    }

    @Test
    void compareIncognitoAndNormal() {
        Calendar calendarNormal = Runner.getCalendarExtractor(pred -> readCalendarFromFile(pred, "calendar2021_normal.txt")).getCalendar();
        Calendar calendarIncognito = Runner.getCalendarExtractor(pred -> readCalendarFromFile(pred, "calendar2021_incognito.txt")).getCalendar();
        System.out.println(calendarNormal.minCountPrintable());
        System.out.println(calendarIncognito.minCountPrintable());
        assertEquals(calendarNormal.size(), calendarIncognito.size());
        for (int i = 0; i < calendarNormal.size(); i++) {
            if (calendarNormal.getDay(i).getColor() != calendarIncognito.getDay(i).getColor()) {
                System.out.println(calendarNormal.getDay(i));
                System.out.println(calendarIncognito.getDay(i));
            }
        }
    }

    private void parseYear(String name) {
        CalendarExtractor calendarExtractor = Runner.getCalendarExtractor(pred -> readCalendarFromFile(pred, name));
        Calendar calendar = calendarExtractor.getCalendar();
        System.out.println(calendar);
        System.out.println(calendar.minCountPrintable());
    }

    private void simulateCalendar(Predicate<String> s) {
        readCalendarFromFile(s, "calendar2021.txt");
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
