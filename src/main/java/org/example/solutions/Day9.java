package org.example.solutions;

import org.example.helper.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day9 implements Solution {

    private final List<List<Integer>> input;
    Pattern numberPattern = Pattern.compile("[0-9-]+");

    public Day9() {
        List<String> tmp = Util.readAsListOfStrings("9.txt");
        input = new ArrayList<>();
        for (String line : tmp) {
            Matcher numberMatcher = numberPattern.matcher(line);
            List<Integer> numbers = new ArrayList<>();
            while (numberMatcher.find())
                numbers.add(Integer.parseInt(numberMatcher.group()));
            input.add(numbers);
        }
    }

    @Override
    public String part1() {
        return getSolution("part1").toString();
    }

    @Override
    public String part2() {
        return getSolution("part2").toString();
    }

    public Integer getSolution(String part) {
        int sum = 0;
        for (List<Integer> line : input) {
            Integer lastNumberSum = 0;
            Map<Integer, List<Integer>> levels = new HashMap<>();
            Integer level = 0;
            levels.put(level, line);
            List<Integer> newLevel = new ArrayList<>();
            List<Integer> prevLevel = line;
            while (!allZeros(newLevel)) {
                newLevel = new ArrayList<>();
                for (int i = 0; i < prevLevel.size() - 1; i++)
                    newLevel.add(prevLevel.get(i + 1) - prevLevel.get(i));
                level++;
                levels.put(level, newLevel);
                lastNumberSum += prevLevel.getLast();
                prevLevel = newLevel;
            }
            if (part.equals("part1"))
                sum += lastNumberSum;
            else {
                Integer bottomfirst = 0;
                for (int i = level - 1; i >= 0; i--) {
                    Integer topFirst = levels.get(i).getFirst();
                    bottomfirst = topFirst - bottomfirst;
                }
                sum += bottomfirst;
            }
        }
        return sum;
    }

    private boolean allZeros(List<Integer> list) {
        if (list.isEmpty())
            return false;
        return list.stream().allMatch(i -> i == 0);

    }
}
