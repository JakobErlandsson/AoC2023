package org.example.solutions;

import org.example.helper.Coordinate;
import org.example.helper.Direction;
import org.example.helper.Util;

import java.util.*;

public class Day17 {

    private record Step(Coordinate coordinate, Direction direction) {
    }

    private static class Node implements Comparable<Node> {
        final Coordinate coordinate;
        Node prev;
        Direction dir;
        Integer straightLength;
        Integer weight;

        Node(Coordinate coordinate) {
            this.coordinate = coordinate;
            prev = null;
            dir = null;
            straightLength = 0;
            weight = 10000000;
        }


        @Override
        public int compareTo(Node n) {
            return this.weight - n.weight;
        }
    }

    Map<Coordinate, Integer> heatMap;
    Integer maxX;
    Integer maxY;


    public Day17() {
        List<String> input = Util.readAsListOfStrings("17.txt");
        maxX = input.get(0).length();
        maxY = input.size();
        heatMap = Util.toCoordinateIntMap(input);
    }

    public Integer findPathWithLeastHeat() {
        int sum = 0;
        Coordinate startCoordinate = new Coordinate(0, 0);
        Node n = djikstra(startCoordinate);
        Map<Coordinate, Node> path = new HashMap<>();
        System.out.println(n.weight);
        while (n.prev != null) {
            path.put(n.coordinate, n);
            n = n.prev;
        }
        printPath(path);
        return 0;
    }

    private void printPath(Map<Coordinate, Node> path) {
        for (int y = 0; y < maxY; y++) {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < maxX; x++) {
                Coordinate coordinate = new Coordinate(x, y);
                if (path.containsKey(coordinate))
                    sb.append(switch (path.get(coordinate).dir) {
                        case RIGHT -> " > ";
                        case LEFT -> " < ";
                        case UP -> " ^ ";
                        case DOWN -> " v ";
                    });
                else {
                    sb.append(" ");
                    sb.append(heatMap.get(coordinate));
                    sb.append(" ");
                }
            }
            System.out.println(sb);
            System.out.println();
        }
    }

    private Node djikstra(Coordinate startCoordinate) {
        Coordinate endCoordinate = new Coordinate(maxX - 1, maxY - 1);
        Queue<Node> queue = new PriorityQueue<>();
        Map<Step, Integer> weights = new HashMap<>();
        Map<Coordinate, Node> coordinateNodeMap = new HashMap<>();
        for (Map.Entry<Coordinate, Integer> entry : heatMap.entrySet()) {
            Node n = new Node(entry.getKey());
            if (n.coordinate.equals(startCoordinate)) {
                n.weight = 0;
                n.straightLength = 0;
                n.dir = Direction.DOWN;
            }
            coordinateNodeMap.put(entry.getKey(), n);
        }
        queue.add(coordinateNodeMap.get(startCoordinate));
        while (!queue.isEmpty()) {
            Node n = queue.poll();
            if (n.coordinate.equals(endCoordinate))
                return n;
            weights.put(new Step(n.coordinate, n.dir), n.weight);
            Map<Coordinate, Direction> neighbours = getNeighbours(n);
            for (Map.Entry<Coordinate, Direction> entry : neighbours.entrySet()) {
                Node neighbour = coordinateNodeMap.get(entry.getKey());
                Step step = new Step(neighbour.coordinate, entry.getValue());
                if (!weights.containsKey(step)) {
                    int heat = n.weight + this.heatMap.get(neighbour.coordinate);
                    if (heat < neighbour.weight) {
                        neighbour.weight = heat;
                        neighbour.prev = n;
                        neighbour.dir = entry.getValue();
                        neighbour.straightLength = entry.getValue() == n.dir ? n.straightLength + 1 : 1;
                        queue.add(neighbour);
                    }
                }
            }
        }
        return coordinateNodeMap.get(endCoordinate);
    }

    private Map<Coordinate, Direction> getNeighbours(Node node) {
        Coordinate right = new Coordinate(node.coordinate.x()+ 1, node.coordinate.y());
        Coordinate left = new Coordinate(node.coordinate.x()-1, node.coordinate.y());
        Coordinate down = new Coordinate(node.coordinate.x(), node.coordinate.y()+1);
        Coordinate up = new Coordinate(node.coordinate.x(), node.coordinate.y()-1);
        Map<Coordinate, Direction> map = new HashMap<>(Map.of(
                right, Direction.RIGHT,
                left, Direction.LEFT,
                down, Direction.DOWN,
                up, Direction.UP
        ));
        switch (node.dir) {
            case RIGHT -> {
                map.remove(left);
                if (node.straightLength == 3) map.remove(right);
            }
            case LEFT -> {
                map.remove(right);
                if (node.straightLength == 3) map.remove(left);
            }
            case UP -> {
                map.remove(down);
                if (node.straightLength == 3) map.remove(up);
            }
            case DOWN -> {
                map.remove(up);
                if (node.straightLength == 3) map.remove(down);
            }
        }
        if (right.x() >= maxX) map.remove(right);
        if (left.x() < 0) map.remove(left);
        if (down.y() >= maxY) map.remove(down);
        if (up.y() < 0) map.remove(up);
        return map;
    }


}
