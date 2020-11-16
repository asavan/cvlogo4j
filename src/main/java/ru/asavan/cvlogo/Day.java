package ru.asavan.cvlogo;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by asavan on 22.05.2020.
 */
class Day {
    private Color color;
    private int count;
    private final LocalDate date;

    public Day(Color color, int count, LocalDate date) {
        this.color = color;
        this.count = count;
        this.date = date;
    }

    public Color getColor() {
        return color;
    }

    public int getCount() {
        return count;
    }

    void adjustDay() {
        count = Math.max(0, count - 1);
        color = null;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDateTime getTime(int count) {
        int hours = (count / 6) + 12;
        int minutes = (count % 6) * 10;
        return date.atTime(hours, minutes);
    }

    @Override
    public String toString() {
        return "Day{" +
                "color=" + color +
                ", count=" + count +
                ", date=" + date +
                '}';
    }
}
