package org.example.solutions;

import org.example.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.Character.isDigit;

public class Day1 {

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

    public Day1() throws IOException {
        this.input = Util.readAsListOfStrings("1.txt");
    }

    public Integer getSolution(boolean part2) {
        int sum = 0;
        for (String line : this.input) {
            Integer tens = getFirstDigit(line, part2) * 10;
            Integer digit = getLastDigit(line, part2);
            sum += tens + digit;
        }
        return sum;
    }

    private Integer getFirstDigit(String line, boolean part2) {
        for (int i = 0; i < line.length(); i++) {
            if (isDigit(line.charAt(i)))
                return Character.getNumericValue(line.charAt(i));
            if (part2) {
                if (i < line.length() - 3 && numbers.containsKey(line.substring(i, i + 3)))
                    return numbers.get(line.substring(i, i + 3));
                if (i < line.length() - 4 && numbers.containsKey(line.substring(i, i + 4)))
                    return numbers.get(line.substring(i, i+4));
                if (i < line.length() - 5 && numbers.containsKey(line.substring(i, i + 5)))
                    return numbers.get(line.substring(i, i+5));
            }
        }
        return 0;
    }

    private Integer getLastDigit(String line, boolean part2) {
        for (int i = line.length() - 1; i >= 0; i--) {
            if (isDigit(line.charAt(i)))
                return Character.getNumericValue(line.charAt(i));
            if (part2) {
                if (i > 1 && numbers.containsKey(line.substring(i - 2, i + 1)))
                    return numbers.get(line.substring(i - 2, i + 1));
                if (i > 2 && numbers.containsKey(line.substring(i - 3, i + 1)))
                    return numbers.get(line.substring(i - 3, i + 1));
                if (i > 3 && numbers.containsKey(line.substring(i - 4, i + 1)))
                    return numbers.get(line.substring(i - 4, i + 1));
            }
        }
        return 0;
    }

}
