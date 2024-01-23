package org.example.solutions;

import org.example.helper.Coordinate;
import org.example.helper.Util;

import java.util.*;

public class Day13 implements Solution {

    List<String[]> chunks;

    public Day13() {
        List<String> input = Util.readAsListOfStrings("13.txt");
        chunks = new ArrayList<>();
        List<String> chunk = new ArrayList<>();
        for (String line : input) {
            if (line.isEmpty()) {
                chunks.add(chunk.toArray(new String[0]));
                chunk = new ArrayList<>();
            } else
                chunk.add(line);
        }
        chunks.add(chunk.toArray(new String[0]));
    }

    public Integer getSolution(String part) {
        List<Coordinate> mirrors = new ArrayList<>();
        for (String[] rows : chunks) {
            String[] columns = new String[rows[0].length()];
            for (int x = 0; x < columns.length; x++) {
                StringBuilder column = new StringBuilder();
                for (String row : rows) {
                    column.append(row.charAt(x));
                }
                columns[x] = column.toString();
            }
            List<Integer> verticalMirror = getMirrorPositions(columns);
            List<Integer> horizontalMirror = getMirrorPositions(rows);
            Coordinate mirror = new Coordinate(
                    verticalMirror.isEmpty() ? 0 : verticalMirror.getFirst(),
                    horizontalMirror.isEmpty() ? 0 :horizontalMirror.getFirst()
            );
            if (part.equals("part2")) {
                mirrors.add(newMirror(mirror, rows, columns));
            } else
                mirrors.add(mirror);
        }
        return mirrors.stream().map(this::mirrorValue).reduce(0, Integer::sum);
    }

    private Coordinate newMirror (Coordinate oldMirror, String[] rows, String[] columns) {
        for (int y = 0; y < rows.length; y++) {
            for (int x = 0; x < columns.length; x++) {
                char c = columns[x].charAt(y) == '#' ? '.' : '#';
                String[] newCols = columns.clone();
                String[] newRows = rows.clone();
                String col = columns[x];
                String row = rows[y];
                col = col.substring(0, y) + c + col.substring(y + 1);
                row = row.substring(0, x) + c + row.substring(x + 1);
                newCols[x] = col;
                newRows[y] = row;
                List<Integer> colResult = getMirrorPositions(newCols);
                List<Integer> rowResult = getMirrorPositions(newRows);
                colResult.remove(Integer.valueOf(oldMirror.x()));
                rowResult.remove(Integer.valueOf(oldMirror.y()));
                if (!colResult.isEmpty())
                    return new Coordinate(colResult.getFirst(), 0);
                if (!rowResult.isEmpty())
                    return new Coordinate(0, rowResult.getFirst());
            }
        }
        throw new RuntimeException("No mirror found");
    }

    private Integer mirrorValue(Coordinate c) {
        return c.x() + 100 * c.y();
    }

    private List<Integer> getMirrorPositions(String[] rows) {
        List<Integer> possibleMirrorPositions = new ArrayList<>();
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < rows.length-1; i++) {
            if (rows[i].equals(rows[i+1])) {
                possibleMirrorPositions.add(i);
            }
        }
        for (Integer j : possibleMirrorPositions) {
            int num = j;
            boolean mirror = true;
            for (int i = j + 1; i < rows.length && j >= 0; i++) {
                if (!rows[i].equals(rows[j])) {
                    mirror = false;
                    break;
                }
                j--;
            }
            if (mirror) {
                res.add(num+1);
            }
        }
        return res;
    }

    @Override
    public String part1() {
        return getSolution("part1").toString();
    }

    @Override
    public String part2() {
        return getSolution("part2").toString();
    }
}
