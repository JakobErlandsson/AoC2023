package org.example;

import org.example.solutions.Day1;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger("");

    public static void main(String[] args) {
        try {
            logger.log(Level.INFO, new Day1().getSolution(false).toString());
            logger.log(Level.INFO, new Day1().getSolution(true).toString());
        } catch (IOException e) {
            logger.log(Level.WARNING, "No such file");
        }
    }
}