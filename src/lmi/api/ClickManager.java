package lmi.api;

import haven.Gob;
import haven.Coord;

import lmi.Constant.StatusCode;
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

    // TODO implement this
    public static Coord[] getArea() {
        Coord[] coordArray = new Coord[2];
        coordArray[0] = ClickManager.getCoord();
        coordArray[1] = ClickManager.getCoord();
        return coordArray;
    }
}
