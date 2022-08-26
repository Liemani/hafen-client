package lmi;

import lmi.api.*;

public class Delegate {
    public static void poseDidChange(haven.Gob gob) {
        // Composite::poseDidChange()
    }

    public static void linMoveDidDelete(haven.Gob gob) {
        // LinMove::$linstep::apply()
        notifySelfIfArrived(gob);
    }

    public static void flowerMenuWillAdded(haven.FlowerMenu widget) {
        // FlowerMenu::$_::create()
        FlowerMenuHandler.setWidget(widget);
    }

    public static void flowerMenuDidAdded() {
        // UI::newwidget()
        FlowerMenuHandler.notifyOpen();
    }

    public static void didCloseFlowerMenu() {
        // FlowerMenu::uimsg()
        FlowerMenuHandler.notifyClose();
    }

    public static void didChoosePetal() {
        // FlowerMenu::uimsg()
        FlowerMenuHandler.notifyClose();
    }

    public static void didGetACK(haven.RMessage message) {
        // Session::RWorker::gotack()
        String command = MessageHandler.getCommand(message);
        WaitManager.notifyIfCommandEquals(command);
    }

    // private methods
    private static void notifySelfIfArrived(haven.Gob gob) {
        if (gob == Self.gob()) {
            Self.notifyIfArrived(gob);
        }
    }
}
