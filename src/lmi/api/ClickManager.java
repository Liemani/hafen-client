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

    // getter setter
    public static boolean isWatingInput() { return isWatingInput_; }
    public static void setClickData(haven.ClickData clickData) { clickData_ = clickData; }
    private static void clear_() { isWatingInput_ = false; clickData_ = null; }

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

    // TODO implement this
    public static Coord[] getRange() {
        return null;
    }
}
