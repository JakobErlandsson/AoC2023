package org.example.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {

    private static final String FILE_BASE = "src/main/resources/";

    public static List<String> readAsListOfStrings(String fileName){
        try {
            return Files.readAllLines(Path.of(FILE_BASE + fileName));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public static String readAsString(String fileName){
        try {
            return Files.readString(Path.of(FILE_BASE + fileName));
        } catch (Exception e) {
            return "No such file";
        }
    }

    public static Map<Coordinate, Character> toCoordinateMap(List<String> input) {
        Map<Coordinate, Character> map = new HashMap<>();
        for (int y = 0; y < input.size(); y++) {
            for (int x = 0; x < input.get(y).length(); x++) {
                map.put(new Coordinate(x, y), input.get(y).charAt(x));
            }
        }
        return map;
    }

    public static Map<Coordinate, Integer> toCoordinateIntMap(List<String> input) {
        Map<Coordinate, Integer> map = new HashMap<>();
        for (int y = 0; y < input.size(); y++) {
            for (int x = 0; x < input.get(y).length(); x++) {
                map.put(new Coordinate(x, y), Character.getNumericValue(input.get(y).charAt(x)));
            }
        }
        return map;
    }
}
