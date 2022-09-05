package lmi.graphic;

import haven.Coord;

// constant
import static lmi.Constant.*;

public class Rect {
    // Field
    public Coord origin;
    public Coord size;

    // initializer
    public void init(Coord coord1, Coord coord2) {
        final Coord minCoord =
            Coord.of(coord1)
            .min(coord2);
        final Coord maxCoord =
            Coord.of(coord1)
            .max(coord2);

        this.origin = minCoord;
        this.size = maxCoord.assignSubtract(minCoord);
    }

    // Constructor
    public Rect(Coord coord1, Coord coord2) { this.init(coord1, coord2); }
    public Rect(Coord[] coordArray) { this.init(coordArray[0], coordArray[1]); }

    // Calculated Geometric Properties
    public int width() { return this.size.x; }
    public int height() { return this.size.y; }
    public int minX() { return this.origin.x; }
    public int midX() { return this.origin.x + this.size.x / 2; }
    public int maxX() { return this.origin.x + this.size.x; }
    public int minY() { return this.origin.y; }
    public int midY() { return this.origin.y + this.size.y / 2; }
    public int maxY() { return this.origin.y + this.size.y; }

    // Transform
    public Rect assignExtendToTile() {
        final Coord minCoord = this.origin;
        final Coord maxCoord = this.origin.add(this.size);

        final Coord extendedMinCoord = minCoord.tileMin();
        final Coord extendedMaxCoord = maxCoord.tileMax();

        this.init(extendedMinCoord, extendedMaxCoord);

        return this;
    }

    // Convenient
    public boolean contains(Coord coord) {
        return this.origin.x <= coord.x && coord.x <= this.origin.x + this.size.x
            && this.origin.y <= coord.y && coord.y <= this.origin.y + this.size.y;
    }

    public String toString() {
        return "{ origin: " + origin + ", size: " + size + " }";
    }
}
