package lmi.api;

public class CoordinateHandler {
    public static haven.Coord east1Tile(haven.Coord mapPoint) {
        return mapPoint.add(1024, 0);
    }

    public static haven.Coord west1Tile(haven.Coord mapPoint) {
        return mapPoint.add(-1024, 0);
    }

    public static haven.Coord south1Tile(haven.Coord mapPoint) {
        return mapPoint.add(0, 1024);
    }

    public static haven.Coord north1Tile(haven.Coord mapPoint) {
        return mapPoint.add(0, -1024);
    }

    public static haven.Coord2d east1Tile(haven.Coord2d mapPoint) {
        return mapPoint.add(11.0, 0.0);
    }

    public static haven.Coord2d west1Tile(haven.Coord2d mapPoint) {
        return mapPoint.add(-11.0, 0.0);
    }

    public static haven.Coord2d south1Tile(haven.Coord2d mapPoint) {
        return mapPoint.add(0.0, 11.0);
    }

    public static haven.Coord2d north1Tile(haven.Coord2d mapPoint) {
        return mapPoint.add(0.0, -11.0);
    }
}
