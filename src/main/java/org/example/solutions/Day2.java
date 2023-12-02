package org.example.solutions;

import org.example.Util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day2 {

    private static final String RED = "red";
    private static final String BLUE = "blue";
    private static final String GREEN = "green";
    private final List<String> input;
    private static final Map<String, Integer> limits = Map.of(
            RED, 12,
            BLUE, 14,
            GREEN, 13
    );

    private static final Pattern digitsPattern = Pattern.compile("\\d+");
    private static final Pattern colorPattern = Pattern.compile("[a-z]+");

    public Day2() throws IOException {
        this.input = Util.readAsListOfStrings("2.txt");
    }

    public Integer getSolution(String part) {
        int sum = 0;
        for (String line : input) {
            if (part.equals("part1"))
                sum += isAllowed(line);
            else
                sum += findPower(line);
        }
        return sum;
    }

    private Integer isAllowed(String line) {
        String[] game = line.split(":");
        String[] hands = game[1].split(";");
        for (String hand : hands) {
            Matcher digitsMatcher = digitsPattern.matcher(hand);
            Matcher colorsMatcher = colorPattern.matcher(hand);
            while (digitsMatcher.find() && colorsMatcher.find()) {
                if (limits.get(colorsMatcher.group()) < Integer.parseInt(digitsMatcher.group()))
                    return 0;
            }
        }
        Matcher idMatcher = digitsPattern.matcher(game[0]);
        idMatcher.find();
        return Integer.parseInt(idMatcher.group());
    }

    private Integer findPower(String line) {
        String[] hands = line.split(":")[1].split(";");
        Map<String, Integer> highest = new HashMap<>(Map.of(
                RED, 0,
                BLUE, 0,
                GREEN, 0
        ));
        for (String hand : hands) {
            Matcher digitsMatcher = digitsPattern.matcher(hand);
            Matcher colorsMatcher = colorPattern.matcher(hand);
            while (digitsMatcher.find() && colorsMatcher.find()) {
                String color = colorsMatcher.group();
                Integer number = Integer.parseInt(digitsMatcher.group());
                highest.put(color, max(highest.get(color), number));
            }
        }
        return highest.get(RED) * highest.get(BLUE) * highest.get(GREEN);
    }

    private Integer max(Integer a, Integer b) {
        return a >= b ? a : b;
    }

}
