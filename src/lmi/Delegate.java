package lmi;

import lmi.api.*;

public class Delegate {
    // flowerMenu
    public static void flowerMenuWillAdded(haven.FlowerMenu widget) {
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
        if (gob == lmi.api.Self.gob())
            WaitManager.checkCommandApply(Constant.Command.Custom.MOVE);
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

    public static void flowerMenuDidClosed() {
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

    // etc
    // TODO
    public static void poseDidChanged(haven.Gob gob) {
        // Composite::poseDidChange()
    }
}
