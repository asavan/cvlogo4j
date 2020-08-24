package ru.asavan.cvlogo;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created by asavan on 30.06.2020.
 */
public class ListPredicate implements Predicate<String> {
    private final List<Predicate<String>> predicates;
    private int counter = 0;

    public ListPredicate(List<Predicate<String>> predicates) {
        this.predicates = predicates;
    }

    @Override
    public boolean test(String s) {
        for (Predicate<String> predicate : predicates) {
            if (predicate.test(s)) {
                ++counter;
            }
        }
        return counter >= predicates.size();
    }
}
