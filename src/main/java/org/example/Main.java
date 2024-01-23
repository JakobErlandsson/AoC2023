package org.example;

import org.example.solutions.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Map<Integer, Solution> solutions = new HashMap<>();

    private static void buildMap() {
        solutions.put(1, new Day1());
        solutions.put(2, new Day2());
        solutions.put(3, new Day3());
        solutions.put(4, new Day4());
        solutions.put(5, new Day5());
        solutions.put(6, new Day6());
        solutions.put(7, new Day7());
        solutions.put(8, new Day8());
        solutions.put(9, new Day9());
        solutions.put(10, new Day10());
        solutions.put(11, new Day11());
        solutions.put(12, new Day12());
        solutions.put(13, new Day13());
        solutions.put(14, new Day14());
        solutions.put(15, new Day15());
        solutions.put(16, new Day16());
//        solutions.put(17, new Day17());
        solutions.put(18, new Day18());
//        solutions.put(19, new Day19());
        solutions.put(20, new Day20());
//        solutions.put(21, new Day21());
//        solutions.put(22, new Day22());
//        solutions.put(23, new Day23());
//        solutions.put(24, new Day24());
//        solutions.put(25, new Day25());
    }

    private static void run(int day) {
        if (!solutions.containsKey(day)) {
            System.out.println("No solution exists for Day " + day);
            return;
        }
        long before = System.currentTimeMillis();
        System.out.println("Day " + day + " part 1: " + solutions.get(day).part1() + " in " + (System.currentTimeMillis() - before) + " ms");
        before = System.currentTimeMillis();
        System.out.println("Day " + day + " part 2: " + solutions.get(day).part2() + " in " + (System.currentTimeMillis() - before) + " ms");
        System.out.println();
    }

    public static void main(String[] args) {
        buildMap();
        int day = Integer.parseInt(args[0]);
        if (day == -1)
            for (int i = 1; i <= 25; i++)
                run(i);
        else
            run(day);
    }
}