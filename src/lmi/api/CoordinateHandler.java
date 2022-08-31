package lmi.api;

import lmi.*;

public class CoordinateHandler {
    // convert
    public static haven.Coord convertCoord2dToCoord(haven.Coord2d mapPoint) {
        return mapPoint.floor(haven.OCache.posres);
    }

    public static haven.Coord2d convertCoordToCoord2d(haven.Coord mapPointInIntCoordinate) {
	    return mapPointInIntCoordinate.mul(haven.OCache.posres);
    }

    // center of tile
    public static haven.Coord2d tileCenter(haven.Coord2d point) {
        double x = java.lang.Math.floor(point.x / 11.0) * 11.0 + 5.5;
        double y = java.lang.Math.floor(point.y / 11.0) * 11.0 + 5.5;
        return new haven.Coord2d(x, y);
    }

    public static haven.Coord tileCenter(haven.Coord point) {
        return point.div(1024).mul(1024).add(512, 512);
    }

    // 4 direction 1 tile
    public static haven.Coord eastTile(haven.Coord mapPoint) {
        return mapPoint.add(1024, 0);
    }

    public static haven.Coord westTile(haven.Coord mapPoint) {
        return mapPoint.add(-1024, 0);
    }

    public static haven.Coord southTile(haven.Coord mapPoint) {
        return mapPoint.add(0, 1024);
    }

    public static haven.Coord northTile(haven.Coord mapPoint) {
        return mapPoint.add(0, -1024);
    }

    public static haven.Coord2d eastTile(haven.Coord2d mapPoint) {
        return mapPoint.add(11.0, 0.0);
    }

    public static haven.Coord2d westTile(haven.Coord2d mapPoint) {
        return mapPoint.add(-11.0, 0.0);
    }

    public static haven.Coord2d southTile(haven.Coord2d mapPoint) {
        return mapPoint.add(0.0, 11.0);
    }

    public static haven.Coord2d northTile(haven.Coord2d mapPoint) {
        return mapPoint.add(0.0, -11.0);
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

    // equals
    public static boolean equals(haven.Coord lhs, haven.Coord rhs) {
        return Math.abs(lhs.x - rhs.x) < Constant.COORD2D_PER_COORD
            && Math.abs(lhs.y - rhs.y) < Constant.COORD2D_PER_COORD;
    }

    public static boolean equals(haven.Coord2d lhs, haven.Coord2d rhs) {
        return Math.abs(lhs.x - rhs.x) < Constant.COORD2D_PER_COORD
            && Math.abs(lhs.y - rhs.y) < Constant.COORD2D_PER_COORD;
    }

    public static boolean equals(haven.Coord2d lhs, haven.Coord rhs) {
        final haven.Coord lhsInCoord = CoordinateHandler.convertCoord2dToCoord(lhs);
        return CoordinateHandler.equals(lhsInCoord, rhs);
    }

    public static boolean equals(haven.Coord lhs, haven.Coord2d rhs) {
        return CoordinateHandler.equals(rhs, lhs);
    }
}
