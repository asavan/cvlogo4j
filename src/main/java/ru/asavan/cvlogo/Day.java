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

    public LocalDateTime getTime() {
        return date.atTime(12, 0);
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
