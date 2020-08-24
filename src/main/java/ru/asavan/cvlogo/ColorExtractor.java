package ru.asavan.cvlogo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by asavan on 23.05.2020.
 */
public class ColorExtractor implements Consumer<String> {
    private final List<String> colors = new ArrayList<>();

    @Override
    public void accept(String inputLine) {
        String color = Parser.parseColorMap(inputLine);
        if (color != null) {
            colors.add(color);
        }
    }

    Map<String, Color> getColorMap() {
        return Map.of(
                colors.get(0), Color.NONE,
                colors.get(1), Color.ONE,
                colors.get(2), Color.TWO,
                colors.get(3), Color.TREE,
                colors.get(4), Color.FOUR
        );
    }
}
