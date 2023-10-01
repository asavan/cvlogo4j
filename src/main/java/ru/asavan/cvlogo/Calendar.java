package ru.asavan.cvlogo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asavan on 22.05.2020.
 */
class Calendar {
    private final List<Day> cal;
    private final Map<Color, Integer> minCount = new EnumMap<>(Color.class);
    private final Map<Color, Integer> maxCount = new EnumMap<>(Color.class);
    private int maxCommits = 0;
    private Color fillColor = null;
    private String repString;

    Calendar() {
        cal = new ArrayList<>();
    }

    void add(Day d) {
        cal.add(d);
    }

    void precalc() {
        minCount.put(Color.NONE, 0);
        minCount.put(Color.ONE, 1);
        cal.sort(Comparator.comparing(Day::getDate));
        int max = 0;
        for (Day day : cal) {
            max = Math.max(max, day.getCount());
            if (day.getColor() == null || day.getColor() == Color.NONE) {
                continue;
            }
            if (day.getColor() != Color.ONE) {
                Integer min = minCount.get(day.getColor());
                if (min == null || min > day.getCount()) {
                    minCount.put(day.getColor(), day.getCount());
                }
            }
            Integer maxColor = maxCount.get(day.getColor());
            if (maxColor == null || maxColor < day.getCount()) {
                maxCount.put(day.getColor(), day.getCount());
            }
        }
        maxCommits = max;

        int height = 7;
        int width = cal.size() / height;
        Integer[][] rep = new Integer[height][width];
        int day = 0;
        for (int w = 0; w < width; ++w) {
            for (int h = 0; h < height; ++h) {
                Day d = getDay(day);
                rep[h][w] = (d == null ? null : d.getCount());
                ++day;
            }
        }
        repString = Pictures.printImage(rep);
    }

    int getMaxCommits() {
        return maxCommits;
    }

    Integer getMinCount(Color c) {
        return minCount.get(c);
    }

    Integer getMaxCount(Color c) {
        return maxCount.get(c);
    }

    Day getDay(int index) {
        if (index < 0 || index >= cal.size()) {
            return null;
        }
        return cal.get(index);
    }

    int size() {
        return cal.size();
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public void setMinColor(Color color, int min) {
        minCount.put(color, min);
    }

    public String minCountPrintable() {
        return minCount + "\n" + maxCount;
    }

    Day getDayByDate(LocalDate date) {
        for (Day day : cal) {
            if (day.getDate().equals(date)) {
                return day;
            }
        }
        return null;
    }

    Day getDayByValue(int count) {
        for (Day day : cal) {
            if (day.getCount() == count) {
                return day;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return repString;
    }
}
