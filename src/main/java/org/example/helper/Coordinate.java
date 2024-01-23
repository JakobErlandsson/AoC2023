package org.example.helper;

public record Coordinate(int x, int y) {

    public Coordinate step(Direction direction) {
        return switch (direction) {
            case RIGHT -> new Coordinate(x+1, y);
            case DOWN -> new Coordinate(x, y+1);
            case LEFT -> new Coordinate(x-1, y);
            case UP -> new Coordinate(x, y-1);
        };
    }
}
