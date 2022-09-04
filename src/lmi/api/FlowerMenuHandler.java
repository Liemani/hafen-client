package lmi.api;

import haven.Gob;

import lmi.*;
import lmi.Constant.StatusCode;
import lmi.Constant.Action;
import static lmi.Constant.StatusCode.*;
import static lmi.Constant.Action.Custom.*;
import static lmi.Constant.TimeOut.*;

public class FlowerMenuHandler {
    // field
    private static haven.FlowerMenu widget_;

    // set widget
    public static void setWidget(haven.FlowerMenu widget) { widget_ = widget; }
    public static void clearWidget() { widget_ = null; }

    // choose
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED_INVALID_ARGUMENT
    ///     - SC_FAILED_OPEN_FLOWER_MENU
    ///     - SC_FAILED_MATCH
    ///     - SC_FAILED_OPEN_PROGRESS
    public static StatusCode choose(Gob gob, int meshId, String name) {
        if (gob == null || name == null)
            return SC_FAILED_INVALID_ARGUMENT;

        {
            final StatusCode result = open_(gob, meshId);
            if (result != SC_SUCCEEDED) return result;
        }
        {
            final StatusCode result = choose_(name);
            if (result != SC_SUCCEEDED) {
                close_();
                return result;
            }
        }
        Self.gob().waitMove();
        return ProgressManager.waitProgress();
    }

    // private methods
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED_OPEN_FLOWER_MENU
    private static StatusCode open_(Gob gob, int meshId) {
        if (sendInteractMessage_(gob, meshId) == SC_INTERRUPTED) return SC_INTERRUPTED;
        return waitFlowerMenuOpening();
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED_OPEN_FLOWER_MENU
    private static StatusCode waitFlowerMenuOpening() {
        if (isAdded_()) return SC_SUCCEEDED;
        switch (WaitManager.waitTimeOut(AC_FLOWER_MENU_DID_ADDED, TO_TEMPORARY)) {
            case SC_SUCCEEDED: return SC_SUCCEEDED;
            case SC_INTERRUPTED: return SC_INTERRUPTED;
            case SC_TIME_OUT: return isAdded_() ? SC_SUCCEEDED : SC_FAILED_OPEN_FLOWER_MENU;
            default:
                new Exception().printStackTrace();
                return SC_INTERRUPTED;
        }
    }

    private static boolean isAdded_() {
        return widget_ != null;
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode close_() {
        return sendCloseMessage_();
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED_MATCH
    private static StatusCode choose_(String name) {
        for (haven.FlowerMenu.Petal petal : widget_.opts)
            if (petal.name.contentEquals(name))
                return sendChoosePetalMessage_(petal.num);
        return SC_FAILED_MATCH;
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED_MATCH
    private static StatusCode sendChoosePetalMessage_(int index) {
        final int petalCount = widget_.opts.length;
        if (0 <= index && index < petalCount) {
            return WidgetMessageHandler.sendChoosePetalMessage(widget_, index);
        } else {
            return SC_FAILED_MATCH;
        }
    }

    // send message shadow
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode sendInteractMessage_(Gob gob, int meshId) {
        return WidgetMessageHandler.interact(gob, meshId);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode sendCloseMessage_() {
        return WidgetMessageHandler.sendCloseFlowerMenuMessage(widget_);
    }
}
