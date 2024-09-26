package map;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapTasks {
public static void displayOccurrences(String[] strings) {
    //input {"lpm", "ab", "a", "c", "cb", "cb", "c", "lpm", "lpm"} 
    //output:
    //lpm -> 3
    //c -> 2
    //cb -> 2
    //a -> 1
    //ab -> 1
    HashMap<String, Long> occurrencesMap = getMapOccurrences(strings);
   TreeMap<Long, TreeSet<String>> sortedOccurrencesMap = getSortedOccurrencesMap(occurrencesMap);
   displaySortedOoccurrencesMap(sortedOccurrencesMap);
}

 public static void displayOccurrencesStream(String[] strings) {
        Arrays.stream(strings).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream().sorted((e1, e2) -> {
                    int res = Long.compare(e2.getValue(), e1.getValue());
                    return res == 0 ? e1.getKey().compareTo(e2.getKey()) : res;
                }).forEach(e -> System.out.printf("%s -> %d\n", e.getKey(), e.getValue()));
    }

    private static void displaySortedOoccurrencesMap(TreeMap<Long, TreeSet<String>> sortedOccurrencesMap) {
        sortedOccurrencesMap.forEach(
                (occurrency, treeSet) -> treeSet.forEach(s -> System.out.printf("%s -> %d \n", s, occurrency)));
    }

    private static TreeMap<Long, TreeSet<String>> getSortedOccurrencesMap(HashMap<String, Long> occurrencesMap) {
        TreeMap<Long, TreeSet<String>> result = new TreeMap<>(Comparator.reverseOrder());
        occurrencesMap.entrySet().forEach(e -> result.computeIfAbsent(e.getValue(),
                k -> new TreeSet<>()).add(e.getKey()));
        return result;
    }

    private static HashMap<String, Long> getMapOccurrences(String[] strings) {
        HashMap<String, Long> result = new HashMap<>();
        Arrays.stream(strings).forEach(s -> result.merge(s, 1l, Long::sum));
        return result;
    }

    public static Map<Integer, Integer[]> getGroupingByNumberOfDigits(int[][] array) {

        Map<Integer, List<Integer>> map = streamOfNumbers(array)
                .collect(Collectors.groupingBy(n -> Integer.toString(n).length()));
        return map.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(),
                e -> e.getValue().toArray(Integer[]::new)));

    }

    private static Stream<Integer> streamOfNumbers(int[][] array) {
        return Arrays.stream(array).flatMapToInt(Arrays::stream).boxed();
    }

    public static Map<Integer, Long> getDistributionByNumberOfDigits(int[][] array) {

        return streamOfNumbers(array).collect(Collectors.groupingBy(n -> Integer.toString(n).length(),
                Collectors.counting()));
    }

    public static void displayDigitsDistribution() {
        Integer[] numbers = new Integer[1000_000];
        Random random = new Random();
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = random.nextInt(Integer.MAX_VALUE);
        }
        Map<Integer, Long> map = getDigitDistrSortingDescending(getDigitDistrNotSorted(numbers));
        map.entrySet().stream().forEach(e -> System.out.printf("%s -> %d\n", e.getKey(), e.getValue()));
    }

    private static Map<Integer, Long> getDigitDistrSortingDescending(Map<Integer, Long> map) {
        return map.entrySet().stream().sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private static Map<Integer, Long> getDigitDistrNotSorted(Integer[] numbers) {
        return getStreamOfDigits(numbers).collect(Collectors.groupingBy(n -> n, Collectors.counting()));
    }

    private static Stream<Integer> getStreamOfDigits(Integer[] numbers) {
        return Arrays.stream(numbers).flatMapToInt(i -> String.valueOf(i).chars().map(Character::getNumericValue))
                .boxed();
    }

    public static ParenthesesMaps getParenthesesMaps(Character[][] openCloseParentheses) {
        Map<Character, Character> openCloseMap = Stream.of(openCloseParentheses).
        collect(Collectors.toMap(brackets -> brackets[0], brackets -> brackets[1]));
        Map<Character, Character> closeOpenMap = Stream.of(openCloseParentheses)
                .collect(Collectors.toMap(brackets -> brackets[1], brackets -> brackets[0]));
        return new ParenthesesMaps(openCloseMap, closeOpenMap);
    }
}