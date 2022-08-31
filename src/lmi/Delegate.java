package lmi;

import lmi.api.*;
import lmi.Constant.Command;
import lmi.collection.Array;

// constant
import static lmi.Constant.Command.*;
import static lmi.Constant.Command.Custom.*;

public class Delegate {
    // custom command shadow
    // flowerMenu
    public static void flowerMenuDidCreated(haven.FlowerMenu widget) {
        // FlowerMenu::$_::create()
        FlowerMenuHandler.setWidget(widget);
    }

    public static void flowerMenuDidCanceled() {
        // FlowerMenu::Cancel::ntick()
        WaitManager.notifyCommand(C_CLOSE_FLOWER_MENU);
    }

    public static void flowerMenuDidChosen() {
        // FlowerMenu::Chosen::ntick()
        WaitManager.notifyCommand(C_CLOSE_FLOWER_MENU);
    }

    // linMove
    public static void linMoveDidAdded(haven.Gob gob) {
        // LinMove::$linbeg::apply()
        notifyCommandIfGobIsSelf_(gob, CC_SELF_MOVE_DID_BEGIN);
    }

    public static void linMoveDidDeleted(haven.Gob gob) {
        // LinMove::$linstep::apply()
        notifyCommandIfGobIsSelf_(gob, CC_SELF_MOVE_DID_END);
    }

    // progress
    public static void progressDidAdded(haven.GameUI.Progress widget) {
        // GameUI.uimsg()
        ProgressManager.setWidget(widget);
        WaitManager.notifyCommand(Command.Custom.CC_PROGRESS_DID_ADDED);
    }

    public static void progressDidDestroyed() {
        // GameUI.uimsg()
        ProgressManager.setWidget(null);
        WaitManager.notifyCommand(Command.Custom.CC_PROGRESS_DID_DESTROYED);
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
        WaitManager.notifyCommand(C_CHANGE_CURSOR);
    }

    public static void newWidgetDidAdded(haven.Widget widget) {
        // UI::addwidget()
        if (widget.getClass() == haven.FlowerMenu.class)
            WaitManager.notifyCommand(CC_FLOWER_MENU_DID_ADDED);
        lmi.Debug.describeClassNameHashCodeWithTag("widget: ", widget);
    }

    public static void didGetACK(haven.RMessage message) {
        // Session::RWorker::gotack()
        final String command = MessageHandler.getCommand(message);
        WaitManager.notifyCommand(command);
    }

    // private method
    private static void notifyCommandIfGobIsSelf_(haven.Gob gob, Command.Custom customCommand) {
        if (gob == lmi.api.Self.gob())
            WaitManager.notifyCommand(customCommand);
    }
}
