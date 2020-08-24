package ru.asavan.cvlogo;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by asavan on 23.05.2020.
 */
public class SvgExtractor implements Predicate<String> {
    private boolean needCollect = false;
    private final Consumer<String> consumer;

    public SvgExtractor(Consumer<String> consumer) {
        this.consumer = consumer;
    }

    @Override
    public boolean test(String inputLine) {
        if (inputLine.startsWith("<svg")) {
            needCollect = true;
        }
        if (needCollect) {
            consumer.accept(inputLine);
        }
        if (inputLine.endsWith("</svg>")) {
            needCollect = false;
            return true;
        }
        return false;
    }
}
