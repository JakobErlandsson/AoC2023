package org.example.solutions;

import org.example.helper.Coordinate;
import org.example.helper.Direction;
import org.example.helper.Util;

import java.math.BigInteger;
import java.util.*;

public class Day18 implements Solution{

    List<Coordinate> corners;
    Long trenchSize;
    List<String> input;
    String part;

    public Day18() {
        input = Util.readAsListOfStrings("18.txt");
    }

    private Coordinate step(Coordinate c, Direction d, Integer dist) {
        return switch (d) {
            case RIGHT -> new Coordinate(c.x() + dist, c.y());
            case LEFT -> new Coordinate(c.x() - dist, c.y());
            case UP -> new Coordinate(c.x(), c.y() - dist);
            case DOWN -> new Coordinate(c.x(), c.y() + dist);
        };
    }

    private void digTrench() {
        corners = new ArrayList<>();
        trenchSize = 0L;
        Coordinate at = new Coordinate(0, 0);
        trenchSize++;
        for (String line : input) {
            corners.add(at);
            Direction dir;
            int dist;
            if (part.equals("part2")) {
                String hexString = line.split(" ")[2].replace("(", "").replace(")", "");
                dir = switch (hexString.charAt(hexString.length() - 1)) {
                    case '0' -> Direction.RIGHT;
                    case '1' -> Direction.DOWN;
                    case '2' -> Direction.LEFT;
                    case '3' -> Direction.UP;
                    default -> null;
                };
                dist = Integer.parseInt(hexString.substring(1, hexString.length() - 1), 16);
            } else {
                dir = switch (line.split(" ")[0]) {
                    case "R" -> Direction.RIGHT;
                    case "L" -> Direction.LEFT;
                    case "D" -> Direction.DOWN;
                    case "U" -> Direction.UP;
                    default -> null;
                };
                dist = Integer.parseInt(line.split(" ")[1]);
            }
            at = step(at, dir, dist);
            trenchSize += dist;
        }
    }

    public String getSizeOfTrench() {
        BigInteger area = BigInteger.ZERO;
        for (int i = 0; i < corners.size(); i++) {
            int j = (i + 1) % corners.size();
            long ix = corners.get(i).x();
            long jx = corners.get(j).x();
            long iy = corners.get(i).y();
            long jy = corners.get(j).y();

            area = area.add(BigInteger.valueOf(ix * jy));
            area = area.subtract(BigInteger.valueOf(iy * jx));
        }
        return area.abs().add(BigInteger.valueOf(trenchSize)).divide(BigInteger.TWO).add(BigInteger.ONE).toString();
    }

    @Override
    public String part1() {
        part = "part1";
        digTrench();
        return getSizeOfTrench();
    }

    @Override
    public String part2() {
        part = "part2";
        digTrench();
        return getSizeOfTrench();
    }
}
