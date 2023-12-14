package org.example.solutions;

import org.example.helper.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day12 {


    List<String> input;
    Pattern digitsPattern = Pattern.compile("\\d+");
    Map<Integer, Map<Integer, List<String>>> bitStrings;

    public Day12() {
        input = Util.readAsListOfStrings("12.txt");
    }


    public Long getSolution(String part) {
        long sum = 0;
        bitStrings = new HashMap<>();
        for (String line : input) {
            Matcher digitsMatcher = digitsPattern.matcher(line);
            List<Integer> digits = new ArrayList<>();
            while (digitsMatcher.find())
                digits.add(Integer.parseInt(digitsMatcher.group()));
            String pattern = line.split(" ")[0];
            if (part.equals("part2")) {
                List<Integer> longerList = new ArrayList<>();
                StringBuilder longerPattern = new StringBuilder();
                for (int i = 0; i < 5; i++) {
                    longerList.addAll(digits);
                    longerPattern.append(pattern);
                    longerPattern.append("?");
                }
                digits = longerList;
                pattern = longerPattern.deleteCharAt(longerPattern.length() - 1).toString();
            }
            long valid = findValidStrings(pattern, digits);
            sum += valid;
        }
        return sum;
    }

    private boolean checkBeginning(String sb, Integer length) {
        for (Character c : sb.substring(0, length).toCharArray()) {
            if (c == '.')
                return false;
        }
        return sb.length() == length || sb.charAt(length) == '.' || sb.charAt(length) == '?';
    }

    private record Data(String patter, List<Integer> digits) {
    }

    Map<Data, Long> cache = new HashMap<>();


    private Long findValidStrings(String pattern, List<Integer> digits) {
        Data data = new Data(pattern, digits);
        if (cache.containsKey(data))
            return cache.get(data);
        int sumOfDigits = digits.stream().reduce(0, Integer::sum);
        long nDamaged = pattern.chars().filter(c -> c == '#').count();
        long nAmbiguous = pattern.chars().filter(c -> c == '?').count();

        if (nDamaged > sumOfDigits || nDamaged + nAmbiguous < sumOfDigits) {
            cache.put(data, 0L);
            return 0L;
        }
        if (sumOfDigits == 0) {
            cache.put(data, 1L);
            return 1L;
        }
        if (pattern.charAt(0) == '.') {
            long val = findValidStrings(pattern.substring(1), digits);
            cache.put(data, val);
            return val;
        }
        if (pattern.charAt(0) == '#') {
            int l = digits.getFirst();
            if (checkBeginning(pattern, l)) {
                if (l == pattern.length()) {
                    cache.put(data, 1L);
                    return 1L;
                }
                long val = findValidStrings(pattern.substring(l + 1), digits.subList(1, digits.size()));
                cache.put(data, val);
                return val;
            }
            cache.put(data, 0L);
            return 0L;
        }
        long val1 = findValidStrings(pattern.replaceFirst("\\?", "."), digits);
        long val2 = findValidStrings(pattern.replaceFirst("\\?", "#"), digits);
        return val1 + val2;
    }
}
