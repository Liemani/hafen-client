package lmi;

// ipmort haven
import haven.Gob;
import haven.Coord;

import lmi.Constant.Action;

// import constant
import static lmi.Constant.ExceptionType.*;
import static lmi.Constant.Action.Custom.*;

public class ClickManager {
    // field
    private static boolean _isWaitingMouseDown;
    private static boolean _isWaitingMouseUp;

    private static haven.ClickData _clickData;
    private static Coord _clickPoint;
    private static Rect _selectedArea;

    static void init() { _clear(); }

    // getter setter
    public static boolean isWaitingMouseDown() { return _isWaitingMouseDown; }
    public static boolean isWaitingMouseUp() { return _isWaitingMouseUp; }

    public static void setIsWaitingMouseDown(boolean value) { _isWaitingMouseDown = value; }
    public static void setIsWaitingMouseUp(boolean value) { _isWaitingMouseUp = value; }

    public static void setClickData(haven.ClickData clickData) { _clickData = clickData; }
    public static void setClickPoint(Coord clickPoint) { _clickPoint = clickPoint; }
    public static void setSelectedArea(Rect area) { _selectedArea = area; }


    public static void _clear() {
        _isWaitingMouseDown = false;
        _isWaitingMouseUp = false;
        _clickData = null;
        _clickPoint = null;
    }

    // public method
    public static Gob getGob() {
        _isWaitingMouseDown = true;
        _waitAction(AC_DID_OBJECT_CLICK);

        final haven.Clickable clickable = _clickData.ci;
        Gob clickedGob = null;
        if (clickable.getClass() == Gob.GobClick.class) {
            final Gob.GobClick gobClick = (Gob.GobClick)clickable;
            clickedGob = gobClick.gob;
        } else if (clickable.getClass() == haven.Composited.CompositeClick.class) {
            final haven.Composited.CompositeClick compositeClick = (haven.Composited.CompositeClick)clickable;
            final Gob.GobClick gobClick = compositeClick.gi;
            clickedGob = gobClick.gob;
        }

        _clear();
        return clickedGob;
    }

    public static Coord getCoord() {
        _isWaitingMouseDown = true;
        _waitAction(AC_DID_CLICK);

        final Coord clickPoint = _clickPoint;

        _clear();
        return clickPoint;
    }

    public static Rect getArea() {
        _isWaitingMouseUp = true;
        ObjectShadow.mapView().newSelector();
        _waitAction(AC_DID_AREA_SELECTED);
        ObjectShadow.mapView().destroySelector();

        _clear();
        return _selectedArea;
    }

    public static Array<Gob> getGobArrayInArea() {
        final Rect area = ClickManager.getArea();
        return GobManager.gobArrayInArea(area);
    }

    private static void _waitAction(Action.Custom action) {
        try {
            WaitManager.waitAction(action);
        } catch(Exception e) {
            _clear();
            throw e;
        }
    }
}
