package org.example.solutions;

import org.example.helper.Coordinate;
import org.example.helper.Direction;
import org.example.helper.Util;

import java.util.*;

public class Day16 {

    private record Step(Coordinate at, Direction direction) {
    }

    List<Step> beams;
    Map<Coordinate, Character> cave;
    Integer maxX;
    Integer maxY;
    Set<Coordinate> visited;
    Set<Step> visitedDirections;

    public Day16() {
        List<String> input = Util.readAsListOfStrings("16.txt");
        maxX = input.getFirst().length();
        maxY = input.size();
        cave = Util.toCoordinateMap(input);
    }

    private Integer fireLine(Step start) {
        beams = new ArrayList<>();
        visited = new HashSet<>();
        visitedDirections = new HashSet<>();
        beams.add(start);
        while (!beams.isEmpty()) {
            beams = stepAll(beams);
        }
        return visited.size();
    }

    public Integer getSolution(String part) {
        if (part.equals("part1"))
            return fireLine(new Step(new Coordinate(0, 0), Direction.RIGHT));
        else {
            List<Integer> coverage = new ArrayList<>();
            for (int x = 0; x < maxX; x++) {
                Step start = new Step(new Coordinate(x, 0), Direction.DOWN);
                coverage.add(fireLine(start));
                start = new Step(new Coordinate(x, maxY-1), Direction.UP);
                coverage.add(fireLine(start));
            }
            for (int y = 0; y < maxY; y++) {
                Step start = new Step(new Coordinate(0, y), Direction.RIGHT);
                coverage.add(fireLine(start));
                start = new Step(new Coordinate(maxX-1, y), Direction.LEFT);
                coverage.add(fireLine(start));
            }
            return coverage.stream().reduce(0, Math::max);
        }

    }

    private List<Step> stepAll(List<Step> steps) {
        List<Step> newBeams = new ArrayList<>();
        for (Step beam : steps) {
            Coordinate at = beam.at;
            visited.add(at);
            visitedDirections.add(beam);
            Step up = new Step(new Coordinate(at.x(), at.y() - 1), Direction.UP);
            Step down = new Step(new Coordinate(at.x(), at.y() + 1), Direction.DOWN);
            Step right = new Step(new Coordinate(at.x() + 1, at.y()), Direction.RIGHT);
            Step left = new Step(new Coordinate(at.x() - 1, at.y()), Direction.LEFT);
            switch (beam.direction) {
                case RIGHT -> {
                    switch (cave.get(at)) {
                        case '\\' -> newBeams.add(down);
                        case '/' -> newBeams.add(up);
                        case '|' -> {
                            newBeams.add(up);
                            newBeams.add(down);
                        }
                        default -> newBeams.add(right);
                    }
                }
                case LEFT -> {
                    switch (cave.get(at)) {
                        case '\\' -> newBeams.add(up);
                        case '/' -> newBeams.add(down);
                        case '|' -> {
                            newBeams.add(up);
                            newBeams.add(down);
                        }
                        default -> newBeams.add(left);
                    }
                }
                case UP -> {
                    switch (cave.get(at)) {
                        case '\\' -> newBeams.add(left);
                        case '/' -> newBeams.add(right);
                        case '-' -> {
                            newBeams.add(left);
                            newBeams.add(right);
                        }
                        default -> newBeams.add(up);
                    }
                }
                case DOWN -> {
                    switch (cave.get(at)) {
                        case '\\' -> newBeams.add(right);
                        case '/' -> newBeams.add(left);
                        case '-' -> {
                            newBeams.add(left);
                            newBeams.add(right);
                        }
                        default -> newBeams.add(down);
                    }
                }
            }
            newBeams = removeOutside(newBeams);
        }
        return newBeams;
    }

    private List<Step> removeOutside(List<Step> beams) {
        List<Step> toKeep = new ArrayList<>();
        for (Step beam : beams) {
            if (inside(beam) && !visitedDirections.contains(beam))
                toKeep.add(beam);
        }
        return toKeep;
    }

    private boolean inside(Step beam) {
        return beam.at.x() >= 0 &&
                beam.at.x() < maxX &&
                beam.at.y() >= 0 &&
                beam.at.y() <maxY;
    }
}
