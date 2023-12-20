package org.example.solutions;

import com.sun.jdi.request.DuplicateRequestException;
import org.example.helper.Coordinate;
import org.example.helper.Direction;
import org.example.helper.Util;

import javax.management.DescriptorRead;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Day18 {

    Set<Coordinate> trench;
    List<String> input;
    Set<Coordinate> visited;
    int minY, maxY, minX, maxX;

    public Day18() {
        input = Util.readAsListOfStrings("18.txt");
        visited = new HashSet<>();
        digTrench();
        setSize();
        //printMap();
    }

    private Coordinate step(Coordinate c, Direction d) {
        return switch (d) {
            case RIGHT -> new Coordinate(c.x() + 1, c.y());
            case LEFT -> new Coordinate(c.x() - 1, c.y());
            case UP -> new Coordinate(c.x(), c.y() - 1);
            case DOWN -> new Coordinate(c.x(), c.y() + 1);
        };
    }

    private void digTrench() {
        trench = new HashSet<>();
        Coordinate at = new Coordinate(0, 0);
        trench.add(at);
        for (String line : input) {
            Direction dir = switch (line.split(" ")[0]) {
                case "R" -> Direction.RIGHT;
                case "L" -> Direction.LEFT;
                case "D" -> Direction.DOWN;
                case "U" -> Direction.UP;
                default -> null;
            };
            Integer dist = Integer.parseInt(line.split(" ")[1]);
            for (int i = 0; i < dist; i++) {
                at = step(at, dir);
                trench.add(at);
            }
        }
    }

    private void setSize() {
        minY = Collections.min(trench.stream().map(Coordinate::y).toList());
        minX = Collections.min(trench.stream().map(Coordinate::x).toList());
        maxY = Collections.max(trench.stream().map(Coordinate::y).toList());
        maxX = Collections.max(trench.stream().map(Coordinate::x).toList());
    }

    public Integer getSizeOfTrench() {
        Coordinate startLeft = findLeftEdge();
        Coordinate startRight = findRightEdge();
        Coordinate startTop = findTopEdge();
        Coordinate startBottom = findBottomEdge();
        Queue<Coordinate> queue1 = new ArrayDeque<>();
        Queue<Coordinate> queue2 = new ArrayDeque<>();
        Queue<Coordinate> queue3 = new ArrayDeque<>();
        Queue<Coordinate> queue4 = new ArrayDeque<>();
        visited = new HashSet<>();
        queue1.add(startLeft);
        queue2.add(startBottom);
        queue3.add(startRight);
        queue4.add(startTop);
        List<Queue<Coordinate>> listOfQueues = new ArrayList<>();
        listOfQueues.add(queue1);
        listOfQueues.add(queue2);
        listOfQueues.add(queue3);
        listOfQueues.add(queue4);

        // Process each queue concurrently using multiple threads
        listOfQueues.parallelStream().forEach(queue -> {
            while (!queue.isEmpty()) {
                Coordinate c = queue.poll();
                visited.add(c);
                queue.addAll(getNeighbours(c));
            }
        });
        return visited.size() + trench.size();
    }

    private Coordinate findLeftEdge() {
        List<Coordinate> atMinX = trench.stream().filter(c -> c.x() == minX).toList();
        for (Coordinate c : atMinX) {
            Coordinate right = step(c, Direction.RIGHT);
            if (!trench.contains(right))
                return right;
        }
        throw new RuntimeException("Could not find");
    }

    private Coordinate findRightEdge() {
        List<Coordinate> atMaxX = trench.stream().filter(c -> c.x() == maxX).toList();
        for (Coordinate c : atMaxX) {
            Coordinate left = step(c, Direction.LEFT);
            if (!trench.contains(left))
                return left;
        }
        throw new RuntimeException("Could not find");
    }

    private Coordinate findTopEdge() {
        List<Coordinate> atMinY = trench.stream().filter(c -> c.y() == minY).toList();
        for (Coordinate c : atMinY) {
            Coordinate down = step(c, Direction.DOWN);
            if (!trench.contains(down))
                return down;
        }
        throw new RuntimeException("Could not find");
    }

    private Coordinate findBottomEdge() {
        List<Coordinate> atMaxY = trench.stream().filter(c -> c.y() == maxY).toList();
        for (Coordinate c : atMaxY) {
            Coordinate up = step(c, Direction.UP);
            if (!trench.contains(up))
                return up;
        }
        throw new RuntimeException("Could not find");
    }

    private List<Coordinate> getNeighbours(Coordinate coordinate) {
        List<Coordinate> list = new ArrayList<>();
        Coordinate left = new Coordinate(coordinate.x() - 1, coordinate.y());
        Coordinate right = new Coordinate(coordinate.x() + 1, coordinate.y());
        Coordinate up = new Coordinate(coordinate.x(), coordinate.y() - 1);
        Coordinate down = new Coordinate(coordinate.x(), coordinate.y() + 1);
        if (!(trench.contains(left) || visited.contains(left))) list.add(left);
        if (!(trench.contains(right) || visited.contains(right))) list.add(right);
        if (!(trench.contains(up) || visited.contains(up))) list.add(up);
        if (!(trench.contains(down) || visited.contains(down))) list.add(down);
        return list;
    }

    private void printMap() {
        for (int y = minY - 1; y <= maxY + 1; y++) {
            StringBuilder sb = new StringBuilder();
            for (int x = minX - 1; x <= maxX + 1; x++) {
                Coordinate c = new Coordinate(x, y);
                if (trench.contains(c))
                    sb.append('#');
                else if (visited.contains(c))
                    sb.append('O');
                else
                    sb.append(' ');
            }
            System.out.println(sb);
        }
    }
}
