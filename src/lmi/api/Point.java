package lmi.api;

import lmi.*;

import static lmi.Constant.*;

public class Point {
    // Instance Field
    public int x;
    public int y;

    // Static Value
    public static final Point zero = new Point(0,0);

    // Constructor
    public Point() {}
    public Point(int x, int y) { this.x = x; this.y = y; }

    // Factory
    public static Point of(Point point) { return new Point(point.x, point.y); }
    public static Point of(haven.Coord coord) { return new Point(coord.x, coord.y); }
    public static Point of(haven.Coord2d coord2d) {
        return new Point(
                (int)Math.floor(coord2d.x * COORD_PER_COORD2D),
                (int)Math.floor(coord2d.y * COORD_PER_COORD2D));
    }

    // Convert To Haven Coordinate
    public haven.Coord toCoord() { return new haven.Coord(this.x, this.y); }
    public haven.Coord2d toCoord2d() {
        return new haven.Coord2d(
                this.x * COORD2D_PER_COORD,
                this.y * COORD2D_PER_COORD);
    }

    // Equal To
    public boolean equals(Point point) { return this.x == point.x && this.y == point.y; }

    // Operation
    //  Common Vector
    public Point add(Point point) {
        Point newPoint = new Point();

        newPoint.x = this.x + point.x;
        newPoint.y = this.y + point.y;

        return newPoint;
    }

    public Point subtract(Point point) {
        Point newPoint = new Point();

        newPoint.x = this.x - point.x;
        newPoint.y = this.y - point.y;

        return newPoint;
    }

    public Point multiply(Point point) {
        Point newPoint = new Point();

        newPoint.x = this.x * point.x;
        newPoint.y = this.y * point.y;

        return newPoint;
    }

    public Point divide(Point point) {
        Point newPoint = new Point();

        newPoint.x = this.x / point.x;
        newPoint.y = this.y / point.y;

        return newPoint;
    }

    //  Common Scalar
    public Point add(int value) {
        Point newPoint = new Point();

        newPoint.x = this.x + value;
        newPoint.y = this.y + value;

        return newPoint;
    }

    public Point subtract(int value) {
        Point newPoint = new Point();

        newPoint.x = this.x - value;
        newPoint.y = this.y - value;

        return newPoint;
    }

    public Point multiply(int value) {
        Point newPoint = new Point();

        newPoint.x = this.x * value;
        newPoint.y = this.y * value;

        return newPoint;
    }

    public Point divide(int value) {
        Point newPoint = new Point();

        newPoint.x = this.x / value;
        newPoint.y = this.y / value;

        return newPoint;
    }

    //  Transform Vector
    public Point assignAdd(Point point) {
        this.x += point.x;
        this.y += point.y;
        return this;
    }

    public Point assignSubtract(Point point) {
        this.x -= point.x;
        this.y -= point.y;
        return this;
    }

    public Point assignMultiply(Point point) {
        this.x *= point.x;
        this.y *= point.y;
        return this;
    }

    public Point assignDivide(Point point) {
        this.x /= point.x;
        this.y /= point.y;
        return this;
    }

    //  Transform Scalar
    public Point assignAdd(int value) {
        this.x += value;
        this.y += value;
        return this;
    }

    public Point assignSubtract(int value) {
        this.x -= value;
        this.y -= value;
        return this;
    }

    public Point assignMultiply(int value) {
        this.x *= value;
        this.y *= value;
        return this;
    }

    public Point assignDivide(int value) {
        this.x /= value;
        this.y /= value;
        return this;
    }

    // Convenient
    public Point center() {
        return Point.of(this)
            .assignDivide(TILE_IN_COORD)
            .assignMultiply(TILE_IN_COORD)
            .assignAdd(TILE_IN_COORD / 2);
    }

    // warning: ignore overflow
    public double distance(Point point) {
        final int dx = this.x - point.x;
        final int dy = this.y - point.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
