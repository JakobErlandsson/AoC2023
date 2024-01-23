package org.example.solutions;

import org.example.helper.Coordinate;
import org.example.helper.Direction;
import org.example.helper.Util;

import java.util.*;

public class Day10 implements Solution {

    @Override
    public String part1() {
        return followLine().size() / 2 + "";
    }

    @Override
    public String part2() {
        return findEnclosed().toString();
    }

    private record Step(Coordinate destination, Direction direction) {
    }

    Map<Coordinate, Character> map;
    Coordinate start;
    Integer xMax;
    Integer yMax;

    public Day10() {
        map = new HashMap<>();
        List<String> input = Util.readAsListOfStrings("10.txt");
        yMax = input.size();
        xMax = input.get(0).length();
        for (int y = 0; y < input.size(); y++) {
            String line = input.get(y);
            for (int x = 0; x < line.length(); x++) {
                map.put(new Coordinate(x, y), line.charAt(x));
                if (line.charAt(x) == 'S')
                    start = new Coordinate(x, y);
            }
        }
        map.put(start, getStartSymbol(start));
    }

    private Character getStartSymbol(Coordinate coordinate) {
        Coordinate[] neighbours = {
                new Coordinate(coordinate.x() + 1, coordinate.y()),
                new Coordinate(coordinate.x() - 1, coordinate.y()),
                new Coordinate(coordinate.x(), coordinate.y() - 1),
                new Coordinate(coordinate.x(), coordinate.y() + 1)
        };
        Character right = map.get(neighbours[0]);
        Character left = map.get(neighbours[1]);
        Character up = map.get(neighbours[2]);
        Character down = map.get(neighbours[3]);
        if (right == 'J' || right == '-' || right == '7') {
            if (left == '-' || left == 'L' || left == 'F')
                return '-';
            else if (up == '|' || up == 'F' || up == '7')
                return 'L';
            else // (down == '|' || down == 'J' || down == 'L')
                return 'F';
        } else if (left == '-' || left == 'L' || left == 'F') {
            if (up == '|' || up == 'J' || up == '7')
                return 'J';
            else // (down == '|' || down == 'J' || down == 'L')
                return '7';
        } else //if (up == '|' || up == 'F' || up == '7') {
            return '|';
    }

    private Direction findConnecting(Character symbol) {
        if (symbol == 'F' || symbol == '-')
            return Direction.RIGHT;
        else if (symbol == 'J' || symbol == '|')
            return Direction.UP;
        else if (symbol == '7')
            return Direction.DOWN;
        else // symbol == 'L
            return Direction.RIGHT;
    }

    public Set<Coordinate> followLine() {
        Set<Coordinate> pipe = new HashSet<>();
        Direction next = findConnecting(map.get(start));
        pipe.add(start);
        Coordinate at = step(start, next);
        while (!at.equals(start)) {
            pipe.add(at);
            next = switch (map.get(at)) {
                case 'J' -> next == Direction.RIGHT ? Direction.UP : Direction.LEFT;
                case 'F' -> next == Direction.UP ? Direction.RIGHT : Direction.DOWN;
                case '7' -> next == Direction.RIGHT ? Direction.DOWN : Direction.LEFT;
                case 'L' -> next == Direction.DOWN ? Direction.RIGHT : Direction.UP;
                default -> next;
            };
            at = step(at, next);
        }
        return pipe;
    }

    private List<Coordinate> neighbours(Coordinate c) {
        return List.of(
                new Coordinate(c.x() + 1, c.y()),
                new Coordinate(c.x() - 1, c.y()),
                new Coordinate(c.x(), c.y() + 1),
                new Coordinate(c.x(), c.y() - 1)
        );
    }

    private Coordinate findTopLeft(Set<Coordinate> pipe) {
        for (int y = 0; y < yMax; y++) {
            for (int x = 0; x < xMax; x++) {
                Coordinate c = new Coordinate(x, y);
                if (map.get(c) == 'F' && pipe.contains(c))
                    return c;
            }
        }
        return null;
    }

    private Coordinate step(Coordinate c, Direction d) {
        return switch (d) {
            case RIGHT -> new Coordinate(c.x() + 1, c.y());
            case LEFT -> new Coordinate(c.x() - 1, c.y());
            case UP -> new Coordinate(c.x(), c.y() - 1);
            case DOWN -> new Coordinate(c.x(), c.y() + 1);
        };
    }

    private Coordinate toTheRight(Coordinate c, Direction d) {
        return switch (d) {
            case RIGHT -> step(c, Direction.DOWN);
            case LEFT -> step(c, Direction.UP);
            case UP -> step(c, Direction.RIGHT);
            case DOWN -> step(c, Direction.LEFT);
        };
    }

    public Integer findEnclosed() {
        Set<Coordinate> pipe = followLine();
        Set<Coordinate> cellsInside = new HashSet<>();
        Coordinate topLeft = findTopLeft(pipe);
        Coordinate at = topLeft;
        Direction next = Direction.UP;
        do {
            Direction oldDirection = next;
            next = switch (map.get(at)) {
                case 'J' -> next == Direction.RIGHT ? Direction.UP : Direction.LEFT;
                case 'F' -> next == Direction.UP ? Direction.RIGHT : Direction.DOWN;
                case '7' -> next == Direction.RIGHT ? Direction.DOWN : Direction.LEFT;
                case 'L' -> next == Direction.DOWN ? Direction.RIGHT : Direction.UP;
                default -> next;
            };
            Coordinate toTheRight = toTheRight(at, next);
            Coordinate edgeCase = toTheRight(at, oldDirection);

            Queue<Coordinate> queue = new ArrayDeque<>();
            if (!pipe.contains(toTheRight))
                queue.add(toTheRight);
            if (List.of('J', 'F', 'L', '7').contains(map.get(at)) && !pipe.contains(edgeCase))
                queue.add(edgeCase);

            Set<Coordinate> visited = new HashSet<>();
            while (!queue.isEmpty()) {
                Coordinate c = queue.poll();
                visited.add(c);
                for (Coordinate neighbour : neighbours(c)) {
                    if (!visited.contains(neighbour) && !cellsInside.contains(neighbour) && !pipe.contains(neighbour))
                        queue.add(neighbour);
                }
            }
            at = step(at, next);
            cellsInside.addAll(visited);
        } while (!at.equals(topLeft));
        return cellsInside.size();
    }

}
