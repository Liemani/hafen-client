package lmi.api;

public class CoordinateHandler {
    public static haven.Coord convertCoord2dToCoord(haven.Coord2d point) {
        return point.floor(haven.OCache.posres);
    }

    // center of tile
    public static haven.Coord2d tileCenter(haven.Coord2d point) {
        double x = java.lang.Math.floor(point.x / 11.0) * 11.0 + 5.5;
        double y = java.lang.Math.floor(point.y / 11.0) * 11.0 + 5.5;
        return new haven.Coord2d(x, y);
    }

    // TODO check whether the center is 512 or 511 by printing new Coord2d(5.5, 5.5).floor(posres)
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
}
