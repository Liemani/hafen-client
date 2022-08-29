package lmi;

import lmi.api.*;

public class Delegate {
    // flowerMenu
    public static void flowerMenuDidCreated(haven.FlowerMenu widget) {
        // FlowerMenu::$_::create()
        FlowerMenuHandler.setWidget(widget);
    }

    public static void didGetACK(haven.RMessage message) {
        // Session::RWorker::gotack()
        String command = MessageHandler.getCommand(message);
        System.out.println("[Session::RWorker::gotack() command] \"" + command + "\"");
        WaitManager.checkACKCommand(command);
    }

    // WaitManager::checkCommandApply()
    public static void didMove(haven.Gob gob) {
        // OCache::$move::apply()
        if (gob == lmi.api.Self.gob()) {
            WaitManager.checkCommandApply(Constant.Command.CLICK);
            WaitManager.checkCommandApply(Constant.Command.Custom.MOVE);
        }
    }

    public static void linMoveDidDeleted(haven.Gob gob) {
        // LinMove::$linstep::apply()
        if (gob == lmi.api.Self.gob())
            WaitManager.checkCommandApply(Constant.Command.Custom.MOVE);
    }

    public static void newWidgetDidAdded(haven.Widget widget) {
        // UI::newwidget()
        if (widget.getClass() == haven.FlowerMenu.class)
            WaitManager.checkCommandApply(Constant.Command.CLICK);
    }

    public static void flowerMenuDidCanceled() {
        // FlowerMenu::Cancel::ntick()
        WaitManager.checkCommandApply(Constant.Command.CLOSE_FLOWER_MENU);
    }

    public static void flowerMenuDidChosen() {
        // FlowerMenu::Chosen::ntick()
        WaitManager.checkCommandApply(Constant.Command.CLOSE_FLOWER_MENU);
    }

    public static void cursorDidChanged() {
        // Widget.uimsg()
        WaitManager.checkCommandApply(Constant.Command.CHANGE_CURSOR);
    }

    // progress
    public static void progressDidAdded() {
        // Progress.Progress()
        WaitManager.checkCommandApply(Constant.Command.Custom.PROGRESS_DID_ADDED);
    }

    public static void progressDidDestroyed() {
        // GameUI.uimsg()
        WaitManager.checkCommandApply(Constant.Command.Custom.PROGRESS_DID_DESTROYED);
    }

    // etc
    // TODO
    public static void poseDidChanged(haven.Gob gob) {
        // Composite::poseDidChange()
    }
}
