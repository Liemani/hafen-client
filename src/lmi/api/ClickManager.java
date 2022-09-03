package lmi.api;

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
    public static haven.Gob getGob() {
        isWatingInput_ = true;
        if (WaitManager.waitAction(AC_DID_OBJECT_CLICK) == SC_INTERRUPTED) return null;

        final haven.Clickable clickable = clickData_.ci;
        if (clickable.getClass() != haven.Gob.GobClick.class) return null;
        final haven.Gob.GobClick gobClick = (haven.Gob.GobClick)clickable;
        final haven.Gob clickedGob = gobClick.gob;

        clear_();

        return clickedGob;
    }
}
