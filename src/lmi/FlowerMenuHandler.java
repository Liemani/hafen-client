package lmi;

import haven.Gob;

import lmi.Constant.Action;
import static lmi.Constant.ExceptionType.*;
import static lmi.Constant.Action.Custom.*;
import static lmi.Constant.TimeOut.*;

public class FlowerMenuHandler {
    // field
    private static haven.FlowerMenu _widget;

    // set widget
    public static void setWidget(haven.FlowerMenu widget) { _widget = widget; }
    public static void clearWidget() { _widget = null; }

    // choose
    /// - Throws:
    ///     - ET_FLOWER_MENU_OPEN
    ///     - ET_FLOWER_MENU_MATCH
    ///     - ET_PROGRESS_OPEN
    public static void choose(Gob gob, int meshId, String name) {
        if (gob == null || name == null)
            throw new IllegalArgumentException();

        _open(gob, meshId);
        try {
            _choose(name);
        } catch (LMIException e) {
            _close();
            throw e;
        }
        try {
            Self.gob().waitMove();
        } catch (LMIException e) { if (e.type() == ET_INTERRUPTED) throw e; }
        ProgressManager.waitProgress();
    }

    // private methods
    /// - Throws:
    ///     - ET_FLOWER_MENU_OPEN
    private static void _open(Gob gob, int meshId) {
        _sendInteractMessage(gob, meshId);
        _waitFlowerMenuOpening();
    }

    /// - Throws:
    ///     - ET_FLOWER_MENU_OPEN
    private static void _waitFlowerMenuOpening() {
        if (_isAdded()) return;
        try {
            WaitManager.waitTimeOut(AC_FLOWER_MENU_DID_ADDED, TO_TEMPORARY);
        } catch (LMIException e) {
            if (e.type() != ET_TIME_OUT) throw e;
            if (!_isAdded()) throw new LMIException(ET_FLOWER_MENU_OPEN);
        }
    }

    private static boolean _isAdded() { return (_widget != null); }
    private static void _close() { _sendCloseMessage(); }

    /// - Throws:
    ///     - ET_FLOWER_MENU_MATCH
    private static void _choose(String name) {
        for (haven.FlowerMenu.Petal petal : _widget.opts)
            if (petal.name.contentEquals(name))
                WidgetMessageHandler.sendChoosePetalMessage(_widget, petal.num);
        throw new LMIException(ET_FLOWER_MENU_MATCH);
    }

    /// - Throws:
    ///     - ET_FLOWER_MENU_MATCH
    private static void _sendChoosePetalMessage(int index) {
        final int petalCount = _widget.opts.length;
        if (0 <= index && index < petalCount)
            WidgetMessageHandler.sendChoosePetalMessage(_widget, index);
        else
            throw new LMIException(ET_FLOWER_MENU_MATCH);
    }

    // send message shadow
    private static void _sendInteractMessage(Gob gob, int meshId) {
        WidgetMessageHandler.interact(gob, meshId);
    }

    /// - Throws:
    private static void _sendCloseMessage() {
        WidgetMessageHandler.sendCloseFlowerMenuMessage(_widget);
    }
}
