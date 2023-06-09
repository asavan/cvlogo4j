package ru.asavan.cvlogo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ParserTest {

    @Test
    void testDay() {
        Day d = Parser.parseOneLine("            <rect width=\"11\" height=\"11\" x=\"12\" y=\"0\" class=\"ContributionCalendar-day\" data-date=\"2022-07-03\" data-level=\"2\" rx=\"2\" ry=\"2\">2 contributions on Sunday, July 3, 2022</rect>");
        assertNotNull(d);
        assertEquals(2, d.getCount());
    }

    @Test
    void testDayNo() {
        Day d = Parser.parseOneLine("            <rect width=\"11\" height=\"11\" x=\"12\" y=\"0\" class=\"ContributionCalendar-day\" data-date=\"2022-07-03\" data-level=\"2\" rx=\"2\" ry=\"2\">No contributions on Sunday, July 3, 2022</rect>");
        assertNotNull(d);
        assertEquals(0, d.getCount());
    }
}
