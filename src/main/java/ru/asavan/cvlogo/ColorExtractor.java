package ru.asavan.cvlogo;

/**
 * Created by asavan on 23.05.2020.
 */
public class ColorExtractor {
    public Color getColor(String c) {
        return Color.values()[c.charAt(0) - '0'];
    }
}
