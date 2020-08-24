package ru.asavan.cvlogo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by asavan on 24.05.2020.
 */
public class MemoryCollector implements Consumer<String> {
    private final List<String> arr = new ArrayList<>();
    @Override
    public void accept(String s) {
        arr.add(s);
    }

    public List<String> getArr() {
        return arr;
    }
}
