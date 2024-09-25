package telran.collections;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MapTasks {
    private static final int RANGE_MIN_VALUE = 1;
    private static final int RANGE_MAX_VALUE = Integer.MAX_VALUE;
    private static final long RANGE_LIMIT = 1_000_000;

    public static void displayOccurrences(String[] strings) {
        Map<String, Long> occurrencesMap = getOccurrencesMap(Arrays.stream(strings));
        TreeMap<Long, TreeSet<String>> sortedOccurencesMap = getSortedOccurrencesMap(occurrencesMap);
        displaySortedOccurrencesMap(sortedOccurencesMap);
    }

    public static void displayOccurrencesStream(String[] strings) {
        Arrays.stream(strings).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream().sorted((e1, e2) -> {
                    int res = Long.compare(e2.getValue(), e1.getValue());
                    return res == 0 ? e1.getKey().compareTo(e2.getKey()) : res;
                }).forEach(e -> System.out.printf("%s -> %d\n", e.getKey(), e.getValue()));
    }

    private static <T> void displaySortedOccurrencesMap(TreeMap<Long, TreeSet<T>> sortedOccurrencesMap) {
        sortedOccurrencesMap.forEach(
                (occurrence, elements) -> elements
                        .forEach(element -> System.out.printf("%s -> %d%n", element, occurrence)));
    }

    private static <T extends Comparable<T>> TreeMap<Long, TreeSet<T>> getSortedOccurrencesMap(
            Map<T, Long> occurrencesMap) {
        TreeMap<Long, TreeSet<T>> sortedOccurrencesMap = new TreeMap<>(Comparator.reverseOrder());
        occurrencesMap.entrySet()
                .forEach(e -> sortedOccurrencesMap.computeIfAbsent(e.getValue(), k -> new TreeSet<>()).add(e.getKey()));
        return sortedOccurrencesMap;
    }

    private static <T> Map<T, Long> getOccurrencesMap(Stream<T> stream) {
        return stream.collect(Collectors.groupingBy(t -> t, Collectors.counting()));
    }

    public static Map<Integer, Integer[]> getGroupingByNumberOfDigits(int[][] array) {
        Map<Integer, List<Integer>> map = streamOfNumbers(array)
                .collect(Collectors.groupingBy(n -> Integer.toString(n).length()));
        return map.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().toArray(Integer[]::new)));
    }

    public static Map<Integer, Long> getDistributionByNumberOfDigits(int[][] array) {
        return streamOfNumbers(array)
                .collect(Collectors.groupingBy(n -> Integer.toString(n).length(), Collectors.counting()));
    }

    private static Stream<Integer> streamOfNumbers(int[][] array) {
        return Arrays.stream(array).flatMapToInt(Arrays::stream).boxed();
    }

    public static void displayDigitsDistribution() {
        IntStream digitStream = getDigitStream();
        Map<Integer, Long> occurrencesMap = getOccurrencesMap(digitStream);
        TreeMap<Long, TreeSet<Integer>> sortedOccurrencesMap = getSortedOccurrencesMap(occurrencesMap);
        displaySortedOccurrencesMap(sortedOccurrencesMap);
    }

    public static ParenthesesMaps getParenthesesMaps(Character[][] openCloseParentheses) {
        Map<Character, Character> openCloseMap = Stream.of(openCloseParentheses)
                .collect(Collectors.toMap(pair -> pair[0], pair -> pair[1]));
        Map<Character, Character> closeOpenMap = Stream.of(openCloseParentheses)
                .collect(Collectors.toMap(pair -> pair[1], pair -> pair[0]));
        return new ParenthesesMaps(openCloseMap, closeOpenMap);
    }

    private static Map<Integer, Long> getOccurrencesMap(IntStream intStream) {
        return getOccurrencesMap(intStream.boxed());
    }

    private static IntStream getDigitStream() {
        return new Random().ints(RANGE_MIN_VALUE, RANGE_MAX_VALUE)
                .limit(RANGE_LIMIT)
                .flatMap(number -> IntStream.iterate(number, n -> n > 0, n -> n / 10)
                        .map(n -> n % 10));
    }
}
