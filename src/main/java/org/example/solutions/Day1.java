package org.example.solutions;

import org.example.helper.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Character.isDigit;

public class Day1 implements Solution{

    private final List<String> input;

    private static final Map<String, Integer> numbers = Map.of(
            "one", 1,
            "two", 2,
            "three", 3,
            "four", 4,
            "five", 5,
            "six", 6,
            "seven", 7,
            "eight", 8,
            "nine", 9
    );

    public Day1() {
        this.input = Util.readAsListOfStrings("1.txt");
    }

    @Override
    public String part1() {
        return getSolution("part1").toString();
    }

    @Override
    public String part2() {
        return getSolution("part2").toString();
    }

    private Integer getSolution(String part) {
        int sum = 0;
        for (String line : this.input) {
            List<Integer> digits = getDigits(line, part.equals("part2"));
            sum += digits.getFirst() * 10 + digits.getLast();
        }
        return sum;
    }

    private List<Integer> getDigits(String line, boolean part2) {
        List<Integer> lst = new ArrayList<>();
        for (int i = 0; i < line.length(); i++) {
            if (isDigit(line.charAt(i)))
                lst.add(Character.getNumericValue(line.charAt(i)));
            if (part2) {
                if (i < line.length() - 2 && numbers.containsKey(line.substring(i, i + 3)))
                    lst.add(numbers.get(line.substring(i, i + 3)));
                if (i < line.length() - 3 && numbers.containsKey(line.substring(i, i + 4)))
                    lst.add(numbers.get(line.substring(i, i + 4)));
                if (i < line.length() - 4 && numbers.containsKey(line.substring(i, i + 5)))
                    lst.add(numbers.get(line.substring(i, i + 5)));
            }
        }
        return lst;
    }

}
