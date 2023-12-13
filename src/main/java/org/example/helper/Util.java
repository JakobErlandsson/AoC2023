package org.example.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

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
}
