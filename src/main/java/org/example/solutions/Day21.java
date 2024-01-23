package org.example.solutions;

import org.example.helper.Coordinate;
import org.example.helper.Util;

import java.util.*;

public class Day21 {

    Map<Coordinate, Character> map;

    Coordinate start;
    Integer maxX;
    Integer maxY;

    Map<Coordinate, List<Coordinate>> neighbours;

    public Day21() {
        List<String> input = Util.readAsListOfStrings("21.txt");
        map = Util.toCoordinateMap(input);
        for (Map.Entry<Coordinate, Character> entry : map.entrySet()) {
            if (entry.getValue() == 'S') {
                start = entry.getKey();
                break;
            }
        }
        map.put(start, '.');
        maxX = input.get(0).length();
        maxY = input.size();
        calculateNeighbours();
    }

    private void calculateNeighbours() {
        neighbours = new HashMap<>();
        for (Map.Entry<Coordinate, Character> entry : map.entrySet()) {
            if (entry.getValue() == '.')
                neighbours.put(entry.getKey(), getNeighbours(entry.getKey()));
        }
    }

    private List<Coordinate> getNeighbours(Coordinate c) {
        List<Coordinate> list = new ArrayList<>();
        Coordinate right = new Coordinate(c.x()+1, c.y());
        Coordinate left = new Coordinate(c.x()-1, c.y());
        Coordinate up = new Coordinate(c.x(), c.y()-1);
        Coordinate down = new Coordinate(c.x(), c.y()+1);
        if (right.x() < maxX && map.get(right) == '.')
            list.add(right);
        if (left.x() >= 0 && map.get(left) == '.')
            list.add(left);
        if (up.y() >= 0 && map.get(up) == '.')
            list.add(up);
        if (down.y() < maxY && map.get(down) == '.')
            list.add(down);
        return list;
    }

    public Integer countLocationsAfterNSteps(Integer n) {
        Set<Coordinate> locations = new HashSet<>();
        locations.add(start);
        for (int i = 0; i < n; i++) {
            Set<Coordinate> newLocations = new HashSet<>();
            for (Coordinate location : locations) {
                newLocations.addAll(neighbours.get(location));
            }
            locations = newLocations;
        }
        return locations.size();
    }

}
