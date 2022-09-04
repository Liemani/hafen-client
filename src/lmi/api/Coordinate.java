package lmi.api;

import lmi.*;

import static lmi.Constant.*;

public class Coordinate {
    // Convert
    public static haven.Coord toCoord(haven.Coord2d coord2d) { return coord2d.floor(haven.OCache.posres); }
    public static haven.Coord2d toCoord2d(haven.Coord coord) { return coord.mul(haven.OCache.posres); }

    // Center Of Tile
    public static haven.Coord2d center(haven.Coord2d point) {
        double x = java.lang.Math.floor(point.x / TILE_IN_COORD2D) * TILE_IN_COORD2D + TILE_IN_COORD2D / 2;
        double y = java.lang.Math.floor(point.y / TILE_IN_COORD2D) * TILE_IN_COORD2D + TILE_IN_COORD2D / 2;
        return new haven.Coord2d(x, y);
    }

    public static haven.Coord center(haven.Coord point) {
        return point.div(TILE_IN_COORD).mul(TILE_IN_COORD).add(TILE_IN_COORD / 2, TILE_IN_COORD / 2);
    }

    // 4 direction 1 tile
    public static haven.Coord eastTile(haven.Coord mapPoint) {
        return mapPoint.add(TILE_IN_COORD, 0);
    }

    public static haven.Coord westTile(haven.Coord mapPoint) {
        return mapPoint.add(-TILE_IN_COORD, 0);
    }

    public static haven.Coord southTile(haven.Coord mapPoint) {
        return mapPoint.add(0, TILE_IN_COORD);
    }

    public static haven.Coord northTile(haven.Coord mapPoint) {
        return mapPoint.add(0, -TILE_IN_COORD);
    }

    public static haven.Coord2d eastTile(haven.Coord2d mapPoint) {
        return mapPoint.add(TILE_IN_COORD2D, 0.0);
    }

    public static haven.Coord2d westTile(haven.Coord2d mapPoint) {
        return mapPoint.add(-TILE_IN_COORD2D, 0.0);
    }

    public static haven.Coord2d southTile(haven.Coord2d mapPoint) {
        return mapPoint.add(0.0, TILE_IN_COORD2D);
    }

    public static haven.Coord2d northTile(haven.Coord2d mapPoint) {
        return mapPoint.add(0.0, -TILE_IN_COORD2D);
    }

    // etc
    public static haven.Coord clone(haven.Coord mapPoint) {
        return new haven.Coord(mapPoint.x, mapPoint.y);
    }

    public static haven.Coord2d clone(haven.Coord2d mapPoint) {
        return new haven.Coord2d(mapPoint.x, mapPoint.y);
    }

    public static haven.Coord2d newCoordinateByOffset(haven.Coord2d point, double xOffset, double yOffset) {
        return point.add(xOffset, yOffset);
    }

    public static haven.Coord newCoordinateByOffset(haven.Coord point, int xOffset, int yOffset) {
        return point.add(xOffset, yOffset);
    }

    // Equal To
    public static boolean equals(haven.Coord lhs, haven.Coord rhs) { return lhs.x == rhs.x && lhs.y == rhs.y; }

    public static boolean equals(haven.Coord2d lhs, haven.Coord2d rhs) {
        return Math.abs(lhs.x - rhs.x) < Constant.COORD2D_PER_COORD
            && Math.abs(lhs.y - rhs.y) < Constant.COORD2D_PER_COORD;
    }

    public static boolean equals(haven.Coord2d lhs, haven.Coord rhs) {
        final haven.Coord lhsInCoord = Coordinate.toCoord(lhs);
        return Coordinate.equals(lhsInCoord, rhs);
    }

    public static boolean equals(haven.Coord lhs, haven.Coord2d rhs) {
        return Coordinate.equals(rhs, lhs);
    }
}
