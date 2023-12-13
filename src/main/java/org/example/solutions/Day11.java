package org.example.solutions;

import org.example.helper.Coordinate;
import org.example.helper.Util;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Day11 {

    Map<Integer, Coordinate> galaxies;
    Set<Integer> emptyRows;
    Set<Integer> emptyColumns;
    AtomicInteger counter;
    String part;

    public Day11(String part) {
        List<String> input = Util.readAsListOfStrings("11.txt");
        counter = new AtomicInteger();
        emptyColumns = new HashSet<>();
        this.part = part;
        for (int x = 0; x < input.get(0).length(); x++) {
            emptyColumns.add(x);
        }
        buildMap(input);
    }

    private void buildMap(List<String> input) {
        galaxies = new HashMap<>();
        emptyRows = new HashSet<>();
        for (int y = 0; y < input.size(); y++) {
            String line = input.get(y);
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == '#') {
                    galaxies.put(counter.getAndIncrement(), new Coordinate(x, y));
                    emptyColumns.remove(x);
                }
            }
            if (!line.contains("#"))
                emptyRows.add(y);
        }
    }

    public Long getSolution() {
        long sum = 0;
        for (int i = 0; i < galaxies.size(); i++) {
            for (int j = i+1; j < galaxies.size(); j++) {
                Coordinate galaxy1 = galaxies.get(i);
                Coordinate galaxy2 = galaxies.get(j);
                int lowX = Math.min(galaxy1.x(), galaxy2.x());
                int highX = Math.max(galaxy1.x(), galaxy2.x());
                int lowY = Math.min(galaxy1.y(), galaxy2.y());
                int highY = Math.max(galaxy1.y(), galaxy2.y());
                sum += distX(lowX, highX) + distY(lowY, highY);
            }
        }
        return sum;
    }

    private int distX(int low, int high) {
        int res = 0;
        for (int i = low; i < high; i++) {
            res++;
            if (emptyColumns.contains(i))
                res += part.equals("part1") ? 1 : 999999;
        }
        return res;
    }

    private int distY(int low, int high) {
        int res = 0;
        for (int i = low; i < high; i++) {
            res++;
            if (emptyRows.contains(i))
                res += part.equals("part1") ? 1 : 999999;
        }
        return res;
    }

}
