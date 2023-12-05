package org.example;

import org.example.solutions.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger("");

    public static void main(String[] args) {
        try {
//            System.out.println("Day one part one:\t\t" + new Day1().getSolution("part1").toString());
//            System.out.println("Day one part two:\t\t" + new Day1().getSolution("part2").toString());
//            System.out.println("Day two part one:\t\t" + new Day2().getSolution("part1").toString());
//            System.out.println("Day two part two:\t\t" + new Day2().getSolution("part2").toString());
//            System.out.println("Day three part one:\t\t" + new Day3().getPartNumberSum().toString());
//            System.out.println("Day three part two:\t\t" + new Day3().getSumOfGearRatios().toString());
//            System.out.println("Day four part one:\t\t" + new Day4().getSolution("part1").toString());
//            System.out.println("Day four part two:\t\t" + new Day4().getSolution("part2").toString());
            System.out.println(new Day5().getSolution("part2").toString());
        } catch (IOException e) {
            logger.log(Level.WARNING, "No such file");
        }
    }
}