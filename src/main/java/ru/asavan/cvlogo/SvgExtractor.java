package ru.asavan.cvlogo;

import java.util.function.Predicate;

/**
 * Created by asavan on 23.05.2020.
 */
public class SvgExtractor implements Predicate<String> {
    private boolean needCollect = false;
    private final Predicate<String> predicate;

    public SvgExtractor(Predicate<String> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean test(String inputLine) {
        boolean res = false;
        if (inputLine.contains("<svg")) {
            needCollect = true;
        }
        if (needCollect) {
            res = predicate.test(inputLine);
        }
        if (inputLine.contains("</svg>")) {
            needCollect = false;
            return true;
        }
        return res;
    }
}
