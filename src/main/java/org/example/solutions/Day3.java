package org.example.solutions;

import org.example.Util;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Character.isDigit;

public class Day3 {

    private record Coordinate(int x, int y) { }

    private final List<String> input;

    private final Map<Coordinate, Character> symbolMap;
    private final Map<Coordinate[], Integer> numberIndices;

    public Day3() throws IOException {
        this.input = Util.readAsListOfStrings("3.txt");
        symbolMap = new HashMap<>();
        numberIndices = new HashMap<>();
        buildMaps();
    }

    private void buildMaps() {
        for (int y = 0; y < input.size(); y++) {
            for (int x = 0; x < input.get(y).length(); x++) {
                Character c = input.get(y).charAt(x);
                if (!isDigit(c) && !c.equals('.'))
                    symbolMap.put(new Coordinate(x, y), c);
                if (isDigit(c)) {
                    StringBuilder sb = new StringBuilder();
                    int index = x;
                    int len = 0;
                    while (x < input.get(y).length() && isDigit(input.get(y).charAt(x))) {
                        sb.append(input.get(y).charAt(x));
                        x++;
                        len++;
                    }
                    x--;
                    Coordinate[] coords = new Coordinate[len];
                    for (int i = 0; i < len; i++) {
                        coords[i] = new Coordinate(index + i, y);
                    }
                    numberIndices.put(coords, Integer.parseInt(sb.toString()));
                }
            }
        }
    }

    public Integer getPartNumberSum() {
        Integer sum = 0;
        for (Map.Entry<Coordinate[], Integer> entry : numberIndices.entrySet()) {
            List<Coordinate> neighbours = getNeighbours(entry.getKey()[0], String.valueOf(entry.getValue()).length());
            if (neighbours.stream().anyMatch(symbolMap.keySet()::contains))
                sum += entry.getValue();
        }

        return sum;
    }

    public Integer getSumOfGearRatios() {
        Integer sum = 0;
        for (Map.Entry<Coordinate, Character> symbol : symbolMap.entrySet()) {
            if (symbol.getValue() != '*')
                continue;
            List<Coordinate> neighbours = getNeighbours(symbol.getKey());
            Set<Integer> numbers = new HashSet<>();
            for (Coordinate n : neighbours) {
                for (Map.Entry<Coordinate[], Integer> entry : numberIndices.entrySet()) {
                    if (Arrays.asList(entry.getKey()).contains(n))
                        numbers.add(entry.getValue());
                }
            }
            if (numbers.size() == 2) {
                List<Integer> arr = new ArrayList<>(numbers);
                sum += arr.getFirst() * arr.getLast();
            }
        }
        return sum;
    }

    private List<Coordinate> getNeighbours(Coordinate c, int lengthOfNumber) {
        List<Coordinate> neighbours = new ArrayList<>(lengthOfNumber * 2 + 6);
        for (int i = -1; i < lengthOfNumber + 1; i++) {
            neighbours.add(new Coordinate(c.x + i, c.y - 1));
            neighbours.add(new Coordinate(c.x + i, c.y + 1));
        }
        neighbours.add(new Coordinate(c.x - 1, c.y));
        neighbours.add(new Coordinate(c.x + lengthOfNumber, c.y));
        return neighbours;
    }

    private List<Coordinate> getNeighbours(Coordinate c) {
        List<Coordinate> neighbours = new ArrayList<>(8);
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                neighbours.add(new Coordinate(c.x + i, c.y + j));
            }
        }
        return neighbours;
    }
}
