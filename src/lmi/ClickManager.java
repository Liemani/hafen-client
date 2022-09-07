package lmi;

// ipmort haven
import haven.Gob;
import haven.Coord;

// import constant
import static lmi.Constant.ExceptionType.*;
import static lmi.Constant.Action.Custom.*;

public class ClickManager {
    // field
    private static boolean _isWatingInput = false;
    private static haven.ClickData _clickData = null;
    private static Coord _clickPoint = null;

    // getter setter
    public static boolean isWatingInput() { return _isWatingInput; }
    public static void setClickData(haven.ClickData clickData) { _clickData = clickData; }
    public static void setClickPoint(Coord clickPoint) { _clickPoint = clickPoint; }
    private static void _clear() { _isWatingInput = false; _clickData = null; _clickPoint = null; }

    // public method
    public static Gob getGob() {
        _isWatingInput = true;
        WaitManager.waitAction(AC_DID_OBJECT_CLICK);

        final haven.Clickable clickable = _clickData.ci;
        if (clickable.getClass() == Gob.GobClick.class) {
            final Gob.GobClick gobClick = (Gob.GobClick)clickable;
            final Gob clickedGob = gobClick.gob;

            _clear();
            return clickedGob;
        } else if (clickable.getClass() == haven.Composited.CompositeClick.class) {
            final haven.Composited.CompositeClick compositeClick = (haven.Composited.CompositeClick)clickable;
            final Gob.GobClick gobClick = compositeClick.gi;
            final Gob clickedGob = gobClick.gob;

            _clear();
            return clickedGob;
        }

        return null;
    }

    public static Coord getCoord() {
        _isWatingInput = true;
        WaitManager.waitAction(AC_DID_CLICK);

        final Coord clickPoint = _clickPoint;

        _clear();
        return clickPoint;
    }

    public static Rect getArea() {
        final Coord coord1 = ClickManager.getCoord();
        final Coord coord2 = ClickManager.getCoord();
        return new Rect(coord1, coord2).assignExtendToTile();
    }

    public static Array<Gob> getGobArrayInArea() {
        final Rect area = ClickManager.getArea();
        return GobManager.gobArrayInArea(area);
    }
}
