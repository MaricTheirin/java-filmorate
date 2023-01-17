package ru.yandex.practicum.filmorate.util;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AggregationUtil {

    private AggregationUtil() {
    }

    public static <T> Set<T> mapAggregatedValuesToSet (String aggString, Function<String, T> function) {
        if (aggString == null || aggString.length() < 2 ) {
            return new LinkedHashSet<>();
        }
        return Arrays.stream(aggString.replaceAll("[\\[\\]\\s]", "").split(","))
                .map(function)
                .collect(Collectors.toSet());
    }

}
