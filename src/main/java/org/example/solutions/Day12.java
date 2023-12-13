package org.example.solutions;

import org.example.helper.Util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day12 {


    List<String> input;
    Pattern digitsPattern = Pattern.compile("\\d+");
    Pattern brokenPattern = Pattern.compile("#+");
    Map<Integer, Map<Integer, List<String>>> bitStrings;

    public Day12() {
        input = Util.readAsListOfStrings("12.txt");
    }

    public Integer getSolution(String part) {
        int sum = 0;
        int bitStringsHits = 0;
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
                pattern = longerPattern.toString();
            }
            int nBroken = (int) pattern.chars().filter(c -> c == '#').count();
            int needed = digits.stream().reduce(0, Integer::sum) - nBroken;
            int nUnknowns = (int) pattern.chars().filter(c -> c == '?').count();
            List<String> toTest;
            if (bitStrings.containsKey(nUnknowns) && bitStrings.get(nUnknowns).containsKey(needed)) {
                toTest = bitStrings.get(nUnknowns).get(needed);
                bitStringsHits++;
            }
            else {
                toTest = new ArrayList<>();
                int start = (int) Math.pow(2, needed) - 1;
                int end = calcCeiling(nUnknowns, needed);
                for (int i = start; i <= end; i++) {
                    int bitCount = Integer.bitCount(i);
                    if (bitCount != needed)
                        continue;
                    String sCounter = String.format("%" + nUnknowns + "s", Integer.toBinaryString(i)).replace(" ", "0");
                    toTest.add(sCounter);
                }
                Map<Integer, List<String>> map;
                if (bitStrings.containsKey(nUnknowns))
                    map = bitStrings.get(nUnknowns);
                else
                    map = new HashMap<>();
                map.put(needed, toTest);
                bitStrings.put(nUnknowns, map);
            }
            for (String s : toTest)
                if (verify(createString(pattern, s), digits))
                    sum += 1;
        }
        System.out.println(bitStringsHits);
        return sum;
    }

//    private void findBitStringsForN()

    private int calcCeiling(int length, int nOnes) {
        int sum = 0;
        for (int i = length - nOnes; i < length; i++)
            sum += (int) Math.pow(2, i);
        return sum;
    }

    private String createString(String pattern, String binaryNumber) {
        int counter = 0;
        StringBuilder sb = new StringBuilder();
        for (Character c : pattern.toCharArray()) {
            if (c == '?') {
                sb.append(binaryNumber.charAt(counter) == '0' ? '.' : '#');
                counter++;
            } else
                sb.append(c);
        }
        return sb.toString();
    }

    private boolean verify(String pattern, List<Integer> nBroken) {
        Matcher brokenMatcher = brokenPattern.matcher(pattern);
        List<String> matches = new ArrayList<>();
        while (brokenMatcher.find())
            matches.add(brokenMatcher.group());
        return matches.stream().map(String::length).toList().equals(nBroken);
    }
}
