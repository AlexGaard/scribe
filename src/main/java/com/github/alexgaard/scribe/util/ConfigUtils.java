package com.github.alexgaard.scribe.util;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ConfigUtils {

    private ConfigUtils() {}

    public static Map<String, String> filterMapKeys(Map<String, String> map, String namePrefix) {
        return filterMapKeys(map, key -> key.startsWith(namePrefix));
    }

    public static Map<String, String> filterAndStripPrefixFromMapKeys(Map<String, String> map, String namePrefix) {
        return filterAndMutateMapKeys(map, (key) -> key.substring(namePrefix.length()), key -> key.startsWith(namePrefix));
    }

    public static Map<String, String> filterMapKeys(Map<String, String> map, Predicate<String> filter) {
        Set<Map.Entry<String, String>> entries = map.entrySet()
                .stream()
                .filter(e -> filter.test(e.getKey()))
                .collect(Collectors.toSet());

        return toMap(entries);
    }

    public static Map<String, String> filterAndMutateMapKeys(Map<String, String> map, Function<String, String> keyMutator, Predicate<String> filter) {
        Set<Map.Entry<String, String>> entries = map.entrySet()
                .stream()
                .filter(e -> filter.test(e.getKey()))
                .map(e -> new AbstractMap.SimpleEntry<>(keyMutator.apply(e.getKey()), e.getValue()))
                .collect(Collectors.toSet());

        return toMap(entries);
    }

    public static <K, V> Map<K, V> toMap(Set<Map.Entry<K, V>> entries) {
        return  entries.stream().collect(
                Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (prev, next) -> next, HashMap::new
                ));
    }

}
