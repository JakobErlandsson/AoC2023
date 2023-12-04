package org.example.solutions;

import org.example.Util;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.Character.isDigit;

public class Day3 {

    private record Coordinate(int x, int y) {
    }

    private static class UniqueInteger {
        Integer number;
        Long id;

        UniqueInteger(Integer number, Long id) {
            this.number = number;
            this.id = id;
        }

        public Integer getNumber() {
            return number;
        }
    }

    private final AtomicLong id;

    private final List<String> input;

    private final Map<Coordinate, Character> symbolMap;
    private final Map<Coordinate, UniqueInteger> numberIndices;

    public Day3() throws IOException {
        this.input = Util.readAsListOfStrings("3.txt");
        symbolMap = new HashMap<>();
        numberIndices = new HashMap<>();
        id = new AtomicLong();
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
                    UniqueInteger ui = new UniqueInteger(Integer.parseInt(sb.toString()), id.getAndIncrement());
                    for (int i = 0; i < len; i++) {
                        numberIndices.put(new Coordinate(index + i, y), ui);
                    }
                }
            }
        }
    }

    public Integer getPartNumberSum() {
        Set<UniqueInteger> validParts = new HashSet<>();
        for (Map.Entry<Coordinate, UniqueInteger> entry : numberIndices.entrySet()) {
            List<Coordinate> neighbours = getNeighbours(entry.getKey());
            if (neighbours.stream().anyMatch(symbolMap.keySet()::contains))
                validParts.add(entry.getValue());
        }
        return validParts.stream().map(UniqueInteger::getNumber).reduce(0, Integer::sum);
    }

    public Integer getSumOfGearRatios() {
        int sum = 0;
        for (Map.Entry<Coordinate, Character> symbol : symbolMap.entrySet()) {
            if (symbol.getValue() != '*')
                continue;
            List<Coordinate> neighbours = getNeighbours(symbol.getKey());
            Set<Integer> numbers = new HashSet<>();
            for (Coordinate n : neighbours) {
                UniqueInteger ui = numberIndices.get(n);
                if (ui != null)
                    numbers.add(ui.number);
            }
            if (numbers.size() == 2) {
                List<Integer> arr = new ArrayList<>(numbers);
                sum += arr.getFirst() * arr.getLast();
            }
        }
        return sum;
    }

    private List<Coordinate> getNeighbours(Coordinate c) {
        List<Coordinate> neighbours = new ArrayList<>(8);
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == c.x && j == c.y)
                    continue;
                neighbours.add(new Coordinate(c.x + i, c.y + j));
            }
        }
        return neighbours;
    }
}
