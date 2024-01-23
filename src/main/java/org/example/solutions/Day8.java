package org.example.solutions;

import org.example.helper.Util;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day8 implements Solution {

    private record Direction(String left, String right) {}

    Map<String, Direction> desertMap;
    String instructions;
    Pattern wordPattern = Pattern.compile("\\w+");

    public Day8() {
        List<String> input = Util.readAsListOfStrings("8.txt");
        instructions = input.getFirst();
        buildDesertMap(input);
    }

    private void buildDesertMap(List<String> input) {
        desertMap = new HashMap<>();
        for (int i = 2; i < input.size(); i++) {
            String line = input.get(i);
            List<String> words = new ArrayList<>();
            Matcher wordMatcher = wordPattern.matcher(line);
            while (wordMatcher.find())
                words.add(wordMatcher.group());
            desertMap.put(words.get(0), new Direction(words.get(1), words.get(2)));
        }
    }

    public String part1() {
        Integer steps = 0;
        String at = "AAA";
        while (!at.equals("ZZZ")) {
            char dir = instructions.charAt(steps % instructions.length());
            Direction direction = desertMap.get(at);
            at = dir == 'L' ? direction.left : direction.right;
            steps++;
        }
        return steps.toString();
    }

    public String part2() {
        long steps = 0;
        List<String> startingPoints = desertMap
                .keySet()
                .stream()
                .filter(s -> s.charAt(2) == 'A')
                .toList();
        Long[] firstOccurrence = new Long[startingPoints.size()];
        while(Arrays.stream(firstOccurrence).distinct().anyMatch(Objects::isNull)) {
            for (int i = 0; i < startingPoints.size(); i++) {
                if (firstOccurrence[i] == null && startingPoints.get(i).charAt(2) == 'Z')
                    firstOccurrence[i] = steps;
            }
            char dir = instructions.charAt((int) steps % instructions.length());
            List<Direction> directions = startingPoints
                    .stream()
                    .map(point -> desertMap.get(point))
                    .toList();
            startingPoints = directions
                    .stream()
                    .map(d -> dir == 'L' ? d.left : d.right)
                    .toList();
            steps ++;
        }
        return Arrays.stream(firstOccurrence).distinct().reduce(1L, this::lcm) + "";
    }

    private Long lcm(Long i1, Long i2) {
        long higher = Math.max(i1, i2);
        long lower = Math.min(i1, i2);
        long lcm = higher;
        while (lcm % lower != 0) {
            lcm += higher;
        }
        return lcm;
    }

}
