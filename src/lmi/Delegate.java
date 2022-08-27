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

    // flowerMenu
    public static void flowerMenuWillAdded(haven.FlowerMenu widget) {
        // FlowerMenu::$_::create()
        FlowerMenuHandler.setWidget(widget);
    }

    public static void newWidgetDidAdded() {
        // UI::newwidget()
        WaitManager.notifyIfCommandEquals(Constant.Command.Custom.NEW_WIDGET_DID_ADDED);
    }

    public static void flowerMenuDidClosed() {
        // FlowerMenu::Cancel::ntick()
        WaitManager.notifyIfCommandEquals(Constant.Command.Custom.FLOWER_MENU_CLOSED);
    }

    public static void flowerMenuDidChoosed() {
        // FlowerMenu::Chosen::ntick()
        WaitManager.notifyIfCommandEquals(Constant.Command.Custom.FLOWER_MENU_CHOOSED);
    }

    public static void didGetACK(haven.RMessage message) {
        // Session::RWorker::gotack()
        String command = MessageHandler.getCommand(message);
        System.out.println("[Session::RWorker::gotack() command] \"" + command + "\"");
        WaitManager.notifyIfCommandEquals(command);
    }

    // private methods
    private static void notifySelfIfArrived(haven.Gob gob) {
        if (gob == Self.gob()) {
            Self.notifyIfArrived(gob);
        }
    }
}
