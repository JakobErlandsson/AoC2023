package org.example.solutions;

import org.example.helper.Coordinate;
import org.example.helper.Util;

import java.util.*;

public class Day14 {

    Map<Coordinate, Character> map;
    Map<Integer, List<Integer>> roundRocksCols = new HashMap<>(); // Maps columns to rows
    Map<Integer, List<Integer>> squareRocksCols = new HashMap<>();
    Map<Integer, List<Integer>> roundRocksRows = new HashMap<>(); // Maps rows to columns
    Map<Integer, List<Integer>> squareRocksRows = new HashMap<>();

    Set<Coordinate> state = new HashSet<>();


    Integer nColumns;
    Integer nRows;


    public Day14() {
        List<String> input = Util.readAsListOfStrings("14.txt");
        map = Util.toCoordinateMap(input);
        nColumns = input.get(0).length();
        nRows = input.size();
        for (Map.Entry<Coordinate, Character> entry : map.entrySet()) {
            if (entry.getValue() == '.')
                continue;
            int x = entry.getKey().x();
            int y = entry.getKey().y();
            if (entry.getValue() == 'O')
                state.add(new Coordinate(x, y));
            Map<Integer, List<Integer>> m1 = entry.getValue() == '#' ? squareRocksCols : roundRocksCols;
            Map<Integer, List<Integer>> m2 = entry.getValue() == '#' ? squareRocksRows : roundRocksRows;
            List<Integer> list1 = m1.containsKey(x) ? m1.get(x) : new ArrayList<>();
            List<Integer> list2 = m2.containsKey(y) ? m2.get(y) : new ArrayList<>();
            list1.add(y);
            Collections.sort(list1);
            list2.add(x);
            Collections.sort(list2);
            m1.put(x, list1);
            m2.put(y, list2);
        }
    }

    private Integer findNextLocationDecreasing(Integer location, Integer closestBlock, List<Integer> placedBoulders) {
        if (closestBlock == 0)
            return placedBoulders.isEmpty() ? 0 : placedBoulders.getLast() + 1;
        for (int i = location; i > closestBlock; i--) {
            if (placedBoulders.contains(i))
                return i + 1;
        }
        return closestBlock + 1;
    }

    private Integer findNextLocationIncreasing(Integer location, Integer closestBlock, List<Integer> placedBoulders) {
        if (closestBlock == nRows - 1)
            return placedBoulders.isEmpty() ? nRows - 1 : placedBoulders.getLast() - 1;
        for (int i = location; i < closestBlock; i++) {
            if (placedBoulders.contains(i))
                return i - 1;
        }
        return closestBlock - 1;
    }


    private void tiltEast() {
        Set<Coordinate> newState = new HashSet<>();
        for (int y = 0; y < nRows; y++) {
            List<Integer> blocks = squareRocksRows.get(y) == null ? Collections.emptyList() : squareRocksRows.get(y);
            int finalY = y;
            List<Integer> rocks = new ArrayList<>(state.stream().filter(c -> c.y() == finalY).map(Coordinate::x).toList());
            Collections.sort(rocks);
            Collections.reverse(rocks);
            List<Integer> newLocations = new ArrayList<>();
            for (Integer rock : rocks) {
                int closestBlock = nColumns - 1;
                for (int i = blocks.size() - 1; i >= 0; i--) {
                    if (rock < blocks.get(i))
                        closestBlock = blocks.get(i);
                    else
                        break;
                }
                Integer newX = findNextLocationIncreasing(rock, closestBlock, newLocations);
                newLocations.add(newX);
                newState.add(new Coordinate(newX, y));
            }
        }
        state = newState;
    }

    private void tiltWest() {
        Set<Coordinate> newState = new HashSet<>();
        for (int y = 0; y < nRows; y++) {
            List<Integer> blocks = squareRocksRows.get(y) == null ? Collections.emptyList() : squareRocksRows.get(y);
            int finalY = y;
            List<Integer> rocks = new ArrayList<>(state.stream().filter(c -> c.y() == finalY).map(Coordinate::x).toList());
            Collections.sort(rocks);
            List<Integer> newLocations = new ArrayList<>();
            for (Integer rock : rocks) {
                int closestBlock = 0;
                for (Integer block : blocks)
                    if (rock > block)
                        closestBlock = block;
                    else
                        break;
                Integer newX = findNextLocationDecreasing(rock, closestBlock, newLocations);
                newLocations.add(newX);
                newState.add(new Coordinate(newX, y));
            }
        }
        state = newState;
    }

    private void tiltNorth() {
        Set<Coordinate> newState = new HashSet<>();
        for (int x = 0; x < nColumns; x++) {
            List<Integer> blocks = squareRocksCols.get(x) == null ? Collections.emptyList() : squareRocksCols.get(x);
            int finalX = x;
            List<Integer> rocks = new ArrayList<>(state.stream().filter(c -> c.x() == finalX).map(Coordinate::y).toList());
            Collections.sort(rocks);
            List<Integer> newLocations = new ArrayList<>();
            for (Integer rock : rocks) {
                int closestBlock = 0;
                for (Integer block : blocks)
                    if (rock > block)
                        closestBlock = block;
                    else
                        break;
                Integer newY = findNextLocationDecreasing(rock, closestBlock, newLocations);
                newLocations.add(newY);
                newState.add(new Coordinate(x, newY));
            }
        }
        state = newState;
    }

    private void tiltSouth() {
        Set<Coordinate> newState = new HashSet<>();
        for (int x = 0; x < nColumns; x++) {
            List<Integer> blocks = squareRocksCols.get(x) == null ? Collections.emptyList() : squareRocksCols.get(x);
            int finalX = x;
            List<Integer> rocks = new ArrayList<>(state.stream().filter(c -> c.x() == finalX).map(Coordinate::y).toList());
            Collections.sort(rocks);
            Collections.reverse(rocks);
            List<Integer> newLocations = new ArrayList<>();
            for (Integer rock : rocks) {
                int closestBlock = nRows - 1;
                for (int i = blocks.size() - 1; i >= 0; i--) {
                    if (rock < blocks.get(i))
                        closestBlock = blocks.get(i);
                    else
                        break;
                }
                Integer newY = findNextLocationIncreasing(rock, closestBlock, newLocations);
                newLocations.add(newY);
                newState.add(new Coordinate(x, newY));
            }
        }
        state = newState;
    }

    private void cycle() {
        tiltNorth();
        tiltWest();
        tiltSouth();
        tiltEast();
    }

    private Integer calcLoad() {
        int sum = 0;
        for (int x = 0; x < nColumns; x++) {
            final int finalX = x;
            List<Integer> rocks = new ArrayList<>(state.stream().filter(c -> c.x() == finalX).map(Coordinate::y).toList());
            sum += rocks.stream().map(r -> nRows - r).reduce(0, Integer::sum);
        }
        return sum;
    }

    private boolean isLoop(List<Integer> values) {
        Integer val = values.getLast();
        return values.subList(0, values.size() - 1).contains(val);
    }


    public Integer getSolution(String part) {
        if (part.equals("part1")) {
            tiltNorth();
            return calcLoad();
        }
        int goal = 1000000000;
        Map<Integer, List<Integer>> weightValues = new HashMap<>();
        for (int i = 1; i < goal; i++) { // Will not actually run this long
            cycle();
            int weight = calcLoad();
            List<Integer> numbers;
            if (weightValues.containsKey(weight))
                numbers = weightValues.get(weight);
            else
                numbers = new ArrayList<>();
            numbers.add(i);
            if (numbers.size() >= 3) {
                List<Integer> diffs = new ArrayList<>();
                for (int j = 1; j < numbers.size(); j++)
                    diffs.add(numbers.get(j) - numbers.get(j - 1));
                if (isLoop(diffs)) {
                    int loopSum = 0;
                    Integer val = diffs.getLast();
                    for (int k = diffs.indexOf(val); k < diffs.size() - 1; k++) {
                        loopSum += diffs.get(k);
                    }
                    if (loopSum > 1 && goal % loopSum == i % loopSum)
                        return weight;
                }

            }
            weightValues.put(weight, numbers);
        }
        throw new RuntimeException("No answer found");
    }

    private void printState() {
        for (int y = 0; y < nRows; y++) {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < nColumns; x++) {
                Coordinate c = new Coordinate(x, y);
                if (state.contains(c))
                    sb.append('O');
                else if (map.get(c) == '#')
                    sb.append('#');
                else
                    sb.append(' ');
            }
            System.out.println(sb);
        }
    }
}
