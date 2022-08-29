package lmi;

import lmi.api.*;
import lmi.Constant.Command;

public class Delegate {
    // custom command shadow
    private static final Command.Custom C_SELF_MOVE_DID_STARTED = Command.Custom.SELF_MOVE_DID_STARTED;
    private static final Command.Custom C_SELF_MOVE_DID_ENDED = Command.Custom.SELF_MOVE_DID_ENDED;

    // flowerMenu
    public static void flowerMenuDidCreated(haven.FlowerMenu widget) {
        // FlowerMenu::$_::create()
        FlowerMenuHandler.setWidget(widget);
    }

    // check command
    public static void didGetACK(haven.RMessage message) {
        // Session::RWorker::gotack()
        final String command = MessageHandler.getCommand(message);
        WaitManager.notifyCommand(command);
    }

    public static void linMoveDidAdded(haven.Gob gob) {
        // LinMove::$linbeg::apply()
        notifyCommandIfGobIsSelf_(gob, C_SELF_MOVE_DID_STARTED);
    }

    public static void linMoveDidDeleted(haven.Gob gob) {
        // LinMove::$linstep::apply()
        notifyCommandIfGobIsSelf_(gob, C_SELF_MOVE_DID_ENDED);
    }

    private static void notifyCommandIfGobIsSelf_(haven.Gob gob, Command.Custom customCommand) {
        if (gob == lmi.api.Self.gob())
            WaitManager.notifyCommand(customCommand);
    }

    public static void newWidgetDidAdded(haven.Widget widget) {
        // UI::newwidget()
        if (widget.getClass() == haven.FlowerMenu.class)
            WaitManager.notifyCommand(Command.CLICK);
    }

    public static void flowerMenuDidCanceled() {
        // FlowerMenu::Cancel::ntick()
        WaitManager.notifyCommand(Command.CLOSE_FLOWER_MENU);
    }

    public static void flowerMenuDidChosen() {
        // FlowerMenu::Chosen::ntick()
        WaitManager.notifyCommand(Command.CLOSE_FLOWER_MENU);
    }

    public static void cursorDidChanged() {
        // Widget.uimsg()
        WaitManager.notifyCommand(Command.CHANGE_CURSOR);
    }

    // progress
    public static void progressDidAdded() {
        // Progress.Progress()
        WaitManager.notifyCommand(Command.Custom.PROGRESS_DID_ADDED);
    }

    public static void progressDidDestroyed() {
        // GameUI.uimsg()
        WaitManager.notifyCommand(Command.Custom.PROGRESS_DID_DESTROYED);
    }

    // etc
    // TODO
    public static void poseDidChanged(haven.Gob gob) {
        // Composite::poseDidChange()
    }
}
