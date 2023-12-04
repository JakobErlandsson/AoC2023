package org.example;

import org.example.solutions.Day1;
import org.example.solutions.Day2;
import org.example.solutions.Day3;
import org.example.solutions.Day4;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger("");

    public static void main(String[] args) {
        try {
//            System.out.println("Day one part one: " + new Day1().getSolution("part1").toString());
//            System.out.println("Day one part two: " + new Day1().getSolution("part2").toString());
//            System.out.println("Day two part one: " + new Day2().getSolution("part1").toString());
//            System.out.println("Day two part two: " + new Day2().getSolution("part2").toString());
//            System.out.println(new Day3().getPartNumberSum().toString());
//            System.out.println(new Day3().getSumOfGearRatios().toString());
            System.out.println(new Day4().getSolution("part1").toString());
            System.out.println(new Day4().getSolution("part2").toString());
        } catch (IOException e) {
            logger.log(Level.WARNING, "No such file");
        }
    }
}