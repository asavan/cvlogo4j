package ru.asavan.cvlogo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by asavan on 24.05.2020.
 */
public class MemoryCollector implements Predicate<String> {
    private final List<String> arr = new ArrayList<>();
    @Override
    public boolean test(String s) {
        arr.add(s);
        return false;
    }

    public List<String> getArr() {
        return arr;
    }
}
