package lmi;

import lmi.api.*;
import lmi.Constant.Action;
import lmi.collection.Array;

// constant
import static lmi.Constant.Action.*;
import static lmi.Constant.Action.Custom.*;

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
    public static void linMoveDidAdded(haven.Gob gob) {
        // LinMove::$linbeg::apply()
        WaitManager.notifyAction(gob, AC_MOVE_DID_BEGIN);
    }

    public static void linMoveDidDeleted(haven.Gob gob) {
        // LinMove::$linstep::apply()
        WaitManager.notifyAction(gob, AC_MOVE_DID_END);
    }

    // following
    public static void followingDidAdded(haven.Gob gob) {
        // Following::$follow::apply()
        final haven.Gob target = GobHandler.followingTarget(gob);
        if (target != Self.gob()) return;

        WaitManager.notifyAction(gob, AC_DID_LIFT);
    }

    public static void followingDidDeleted(haven.Gob gob) {
        // Following::$follow::apply()
        final haven.Gob target = GobHandler.followingTarget(gob);
        if (target != Self.gob()) return;

        WaitManager.notifyAction(gob, AC_DID_PUT);
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
    public static void poseDidChanged(haven.Gob gob) {
        // Composite::poseDidChange()
        Array<String> poseArray = GobHandler.poseArray(gob);
        Util.debugPrint(Delegate.class, "gob: " + gob);
        for (String pose : poseArray)
            System.out.println("  " + pose);
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
