package lmi;

// ipmort haven package
import haven.Gob;
import haven.Coord;

// import lmi package
import lmi.Constant.StatusCode;

// import constant
import static lmi.Constant.StatusCode.*;
import static lmi.Constant.Action.Custom.*;

public class ClickManager {
    // field
    private static boolean isWatingInput_ = false;
    private static haven.ClickData clickData_ = null;
    private static Coord clickPoint_ = null;

    // getter setter
    public static boolean isWatingInput() { return isWatingInput_; }
    public static void setClickData(haven.ClickData clickData) { clickData_ = clickData; }
    public static void setClickPoint(Coord clickPoint) { clickPoint_ = clickPoint; }
    private static void clear_() { isWatingInput_ = false; clickData_ = null; clickPoint_ = null; }

    // public method
    public static Gob getGob() {
        isWatingInput_ = true;
        if (WaitManager.waitAction(AC_DID_OBJECT_CLICK) == SC_INTERRUPTED) return null;

        final haven.Clickable clickable = clickData_.ci;
        if (clickable.getClass() == Gob.GobClick.class) {
            final Gob.GobClick gobClick = (Gob.GobClick)clickable;
            final Gob clickedGob = gobClick.gob;

            clear_();
            return clickedGob;
        } else if (clickable.getClass() == haven.Composited.CompositeClick.class) {
            final haven.Composited.CompositeClick compositeClick = (haven.Composited.CompositeClick)clickable;
            final Gob.GobClick gobClick = compositeClick.gi;
            final Gob clickedGob = gobClick.gob;

            clear_();
            return clickedGob;
        }

        return null;
    }

    public static Coord getCoord() {
        isWatingInput_ = true;
        if (WaitManager.waitAction(AC_DID_CLICK) == SC_INTERRUPTED) return null;

        final Coord clickPoint = clickPoint_;

        clear_();
        return clickPoint;
    }

    public static Rect getArea() {
        final Coord coord1 = ClickManager.getCoord();
        final Coord coord2 = ClickManager.getCoord();
        return new Rect(coord1, coord2).assignExtendToTile();
    }

    public static Array<Gob> getGobArrayInArea() {
        final Rect area = ClickManager.getArea();
        return GobManager.getGobArrayInArea(area);
    }
}
