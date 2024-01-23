package org.example.solutions;

import org.example.helper.Util;

import java.io.IOException;
import java.util.*;

public class Day4 implements Solution{
    private final List<String> input;

    public Day4() {
        this.input = Util.readAsListOfStrings("4.txt");
    }



    public Integer getSolution(String part) {
        int sum = 0;
        int[] multiplier = new int[input.size()];
        for (int k = 0; k < input.size(); k++) {
            multiplier[k] += 1;
            String numbers = input.get(k).split(":")[1];
            List<String> tmpHand = new ArrayList<>(Arrays.stream(numbers.split(" \\| ")[0].split(" ")).toList());
            List<String> tmpWinningNumbers = new ArrayList<>(Arrays.stream(numbers.split(" \\| ")[1].split(" ")).toList());
            tmpHand.removeAll(List.of(""));
            tmpWinningNumbers.removeAll(List.of(""));
            List<Integer> hand = new ArrayList<>(tmpHand.stream().map(Integer::parseInt).toList());
            List<Integer> winningNumbers = new ArrayList<>(tmpWinningNumbers.stream().map(Integer::parseInt).toList());
            Collections.sort(hand);
            Collections.sort(winningNumbers);
            int matches = 0;
            for (Integer i : hand) {
                for (Integer j : winningNumbers) {
                    if (i.equals(j)) {
                        matches++;
                        break;
                    }
                }
            }
            for (int i = 1; i < matches + 1; i++)
                multiplier[k + i] += multiplier[k];
            if (part.equals("part1") && matches > 0) {
                sum += (int) Math.pow(2, matches - 1);
            }
        }
        if (part.equals("part2"))
            sum = Arrays.stream(multiplier).reduce(0, Integer::sum);
        return sum;
    }

    @Override
    public String part1() {
        return getSolution("part1").toString();
    }

    @Override
    public String part2() {
        return getSolution("part2").toString();
    }
}
