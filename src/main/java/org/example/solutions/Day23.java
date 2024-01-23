package org.example.solutions;

import org.example.helper.Coordinate;
import org.example.helper.Util;

import java.util.*;
import java.util.stream.Stream;

public class Day23 {

    Map<Coordinate, Character> map;

    Map<Coordinate, List<Coordinate>> neighbours;

    Coordinate start = new Coordinate(1, 1);
    Coordinate end;

    Integer maxX, maxY;

    public Day23() {
        List<String> input = Util.readAsListOfStrings("23.txt");
        map = Util.toCoordinateMap(input);
        map.put(new Coordinate(1, 0), '#');
        maxX = input.get(0).length();
        maxY = input.size();
        neighbours = new HashMap<>();
        for (Coordinate c : map.keySet().stream().filter(k -> map.get(k) == '.').toList()) {
            if (c.y() == maxY - 1)
                end = c;
            neighbours.put(c, getNeighbours(c));
        }
        Set<Set<Coordinate>> paths = findAllPaths();
        for (Set<Coordinate> path : paths) {
            System.out.println(path.size());
        }
    }


    private Set<Set<Coordinate>> findAllPaths() {
        Set<Set<Coordinate>> paths = new HashSet<>();
        Stack<Coordinate> stack = new Stack<>();
        Set<Coordinate> visited = new HashSet<>();
        stack.add(start);
        while (!stack.isEmpty()) {
            Coordinate c = stack.pop();
            switch (map.get(c)) {
                case '>' -> {
                    visited.add(c);
                    c = new Coordinate(c.x()+1, c.y());
                }
                case '<' -> {
                    visited.add(c);
                    c = new Coordinate(c.x()-1, c.y());
                }
                case '^' -> {
                    visited.add(c);
                    c = new Coordinate(c.x(), c.y()-1);
                }
                case 'v' -> {
                    visited.add(c);
                    c = new Coordinate(c.x(), c.y()+1);
                }
            }
            if (c.equals(end))
                paths.add(new HashSet<>(visited));
            else
                visited.add(c);
            for (Coordinate n : neighbours.get(c)) {
                if (!visited.contains(n))
                    stack.add(n);
            }
        }
        return paths;
    }

    private List<Coordinate> getNeighbours(Coordinate c) {
        List<Coordinate> list = new ArrayList<>();
        Coordinate right = new Coordinate(c.x() + 1, c.y());
        Coordinate left = new Coordinate(c.x() - 1, c.y());
        Coordinate up = new Coordinate(c.x(), c.y() - 1);
        Coordinate down = new Coordinate(c.x(), c.y() + 1);

        if (right.x() < maxX && (map.get(right) == '.' || map.get(right) == '>'))
            list.add(right);
        if (left.x() >= 0 && (map.get(left) == '.' || map.get(left) == '<'))
            list.add(left);
        if (up.y() >= 0 && (map.get(up) == '.' || map.get(up) == '^'))
            list.add(up);
        if (down.y() < maxY && (map.get(down) == '.' || map.get(down) == 'v'))
            list.add(down);
        return list;
    }
}
