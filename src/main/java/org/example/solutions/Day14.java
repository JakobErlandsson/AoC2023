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
    Integer load;


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
            list2.add(x);
            m1.put(x, list1);
            m2.put(y, list2);
        }
    }

    private Integer findNextLocationDecreasing(Integer location, List<Integer> blocks, List<Integer> placedBoulders) {
        for (int i = location; i >= 0; i--) {
            if (blocks.contains(i) || placedBoulders.contains(i))
                return i+1;
        }
        return 0;
    }

    private Integer findNextLocationIncreasing(Integer location, List<Integer> blocks, List<Integer> placedBoulders) {
        for (int i = location; i < nRows; i++) {
            if (blocks.contains(i) || placedBoulders.contains(i))
                return i-1;
        }
        return nRows-1;
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
                Integer newX = findNextLocationIncreasing(rock, blocks, newLocations);
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
                Integer newX = findNextLocationDecreasing(rock, blocks, newLocations);
                newLocations.add(newX);
                newState.add(new Coordinate(newX, y));
            }
        }
        state = newState;
    }

    private void tiltNorth() {
        Set<Coordinate> newState = new HashSet<>();
        int load = 0;
        for (int x = 0; x < nColumns; x++) {
            List<Integer> blocks = squareRocksCols.get(x) == null ? Collections.emptyList() : squareRocksCols.get(x);
            int finalX = x;
            List<Integer> rocks = new ArrayList<>(state.stream().filter(c -> c.x() == finalX).map(Coordinate::y).toList());
            Collections.sort(rocks);
            List<Integer> newLocations = new ArrayList<>();
            for (Integer rock : rocks) {
                Integer newY = findNextLocationDecreasing(rock, blocks, newLocations);
                newLocations.add(newY);
                newState.add(new Coordinate(x, newY));
            }
            load += newLocations.stream().map(r -> nRows - r).reduce(0, Integer::sum);
        }
        state = newState;
        this.load = load;
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
                Integer newY = findNextLocationIncreasing(rock, blocks, newLocations);
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


    public Integer getSolution(String part) {
        int goal = 1000000000;
        Map<Integer, List<Integer>> weightCycles = new HashMap<>();
        Set<Integer> differences = new HashSet<>();
        for (int i = 1; i < 500; i++) {
            cycle();
            if (part.equals("part1"))
                return load;
            int weight = calcLoad();
            List<Integer> numbers;
            if (weightCycles.containsKey(weight))
                numbers = weightCycles.get(weight);
            else
                numbers = new ArrayList<>();
            numbers.add(i);
            weightCycles.put(weight, numbers);
        }
        for (Map.Entry<Integer, List<Integer>> entry : weightCycles.entrySet()) {
            if (entry.getValue().size() > 1) {
                List<Integer> diffs = new ArrayList<>();
                for (int i = 1; i < entry.getValue().size(); i++)
                    diffs.add(entry.getValue().get(i) - entry.getValue().get(i-1));
                System.out.println(entry.getKey() + "\t" + Arrays.toString(entry.getValue().toArray()) + "\t" + Arrays.toString(diffs.toArray()));
            }
        }
        throw new RuntimeException("No answer found");
    }

    private void printState() {
        for(int y = 0; y < nRows; y++) {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < nColumns; x++) {
                if (map.get(new Coordinate(x, y)) == '#')
                    sb.append("⬜️");
                else if (state.contains(new Coordinate(x, y)))
                    sb.append("\uD83D\uDD34");
                else
                    sb.append("⬛️");
            }
            System.out.println(sb);
        }
        System.out.println(calcLoad());

    }
}
