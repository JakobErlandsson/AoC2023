package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Util {

    private static final String FILE_BASE = "src/main/resources/";

    public static List<String> readAsListOfStrings(String fileName) throws IOException {
        return Files.readAllLines(Path.of(FILE_BASE + fileName));
    }

    public static String readAsString(String fileName) throws IOException {
        return Files.readString(Path.of(FILE_BASE + fileName));
    }

    public static Integer readAsInteger(String fileName) throws IOException {
        return Integer.valueOf(readAsString(fileName));
    }

    public static List<Integer> readAsListOfIntegers(String fileName) throws IOException{
        return readAsListOfStrings(fileName)
                .stream()
                .map(Integer::valueOf)
                .toList();
    }
}
