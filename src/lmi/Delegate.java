package lmi;

import haven.Gob;
import haven.Coord;

import lmi.api.*;
import lmi.Constant.Action;
import lmi.collection.Array;

// constant
import static lmi.Constant.Action.*;
import static lmi.Constant.Action.Custom.*;
import static lmi.Constant.Input.Mouse.*;

// resource
import static lmi.Constant.gfx.borka.*;

public class Delegate {
    // widget
    public static void newWidgetDidAdded(haven.Widget widget) {
        // UI::addwidget()
        if (widget.getClass() == haven.FlowerMenu.class)
            WaitManager.notifyAction(AC_FLOWER_MENU_DID_ADDED);
        lmi.Debug.describeClassNameHashCodeWithTag("widget: ", widget);
    }

    // flowerMenu
    public static void flowerMenuDidCreated(haven.FlowerMenu widget) {
        // FlowerMenu::$_::create()
        FlowerMenuHandler.setWidget(widget);
    }

    public static void flowerMenuDidCanceled() {
        // FlowerMenu::Cancel::ntick()
        WaitManager.notifyAction(A_CLOSE_FLOWER_MENU);
    }

    public static void flowerMenuDidChosen() {
        // FlowerMenu::Chosen::ntick()
        WaitManager.notifyAction(A_CLOSE_FLOWER_MENU);
    }

    public static void flowerMenuDidDestroyed() {
        // FlowerMenu::Chosen::ntick()
        // FlowerMenu::Cancel::ntick()
        FlowerMenuHandler.clearWidget();
    }

    // linMove
    public static void linMoveDidAdded(Gob gob) {
        // LinMove::$linbeg::apply()
        WaitManager.notifyAction(gob, AC_MOVE_DID_BEGIN);
    }

    public static void linMoveDidDeleted(Gob gob) {
        // LinMove::$linstep::apply()
        WaitManager.notifyAction(gob, AC_MOVE_DID_END);
    }

    // following
    public static void followingDidAdded(Gob gob) {
        // Following::$follow::apply()
        final Gob target = gob.followingTarget();
        if (target != Self.gob()) return;

        WaitManager.notifyAction(target, AC_DID_LIFT);
    }

    public static void followingDidDeleted(Gob gob) {
        // Following::$follow::apply()
        final Gob target = gob.followingTarget();
        if (target != Self.gob()) return;

        WaitManager.notifyAction(target, AC_DID_PUT);
    }

    // progress
    public static void progressDidAdded(haven.GameUI.Progress widget) {
        // GameUI.uimsg()
        ProgressManager.setWidget(widget);
        WaitManager.notifyAction(AC_PROGRESS_DID_ADDED);
    }

    public static void progressDidDestroyed() {
        // GameUI.uimsg()
        ProgressManager.setWidget(null);
        WaitManager.notifyAction(AC_PROGRESS_DID_DESTROYED);
    }

    // etc
    public static void poseDidChanged(Gob gob) {
        // Composite::poseDidChange()
        if (gob.hasPose(RN_IDLE))
            WaitManager.notifyAction(gob, AC_DID_PUT);
    }

    public static boolean didClicked(haven.Coord2d coord2d, int mouseButton, haven.ClickData clickData) {
        // MapView::Click::hit()
        if (!ClickManager.isWatingInput()) return false;
        if (mouseButton != IM_LEFT) return false;

        if (clickData != null) {
            ClickManager.setClickData(clickData);
            WaitManager.notifyAction(AC_DID_OBJECT_CLICK);
        }

        ClickManager.setClickPoint(Coord.of(coord2d));
        WaitManager.notifyAction(AC_DID_CLICK);
        return true;
    }

    public static void cursorDidChanged() {
        // Widget.uimsg()
        WaitManager.notifyAction(A_CHANGE_CURSOR);
    }

    public static void didGetACK(haven.RMessage message) {
        // Session::RWorker::gotack()
        final String command = MessageHandler.getAction(message);
        Util.debugPrint(Delegate.class, "command: " + command);
        WaitManager.notifyAction(command);
    }
}
