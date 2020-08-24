package ru.asavan.cvlogo;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by asavan on 23.05.2020.
 */
public class LegendExtractor implements Predicate<String> {
    private boolean needCollect = false;
    private final Consumer<String> consumer;

    public LegendExtractor(Consumer<String> consumer) {
        this.consumer = consumer;
    }

    @Override
    public boolean test(String inputLine) {
        if (inputLine.trim().startsWith("<ul class=\"legend\">")) {
            needCollect = true;
        }
        if (needCollect) {
            consumer.accept(inputLine);
        }
        if (inputLine.endsWith("</ul>")) {
            needCollect = false;
            return true;
        }
        return false;
    }
}
