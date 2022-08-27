package lmi.api;

import lmi.*;

public class FlowerMenuHandler {
    // assume widget_ never collision
    private static haven.FlowerMenu widget_;

    // set widget
    public static void setWidget(haven.FlowerMenu widget) {
        widget_ = widget;
    }

    // choose
    public static boolean choose(haven.Gob gob, int meshId, String name) {
        if (gob == null || name == null) return false;

        if (!open_(gob, meshId, name)) return false;
        if (choose_(name)) return true;

        System.out.println("[flower menu choose failed]");
        if (!close_()) System.out.println("[flower menu close failed]");
        return false;
    }

    // private methods
    private static void clear_() { widget_ = null; }

    private static boolean open_(haven.Gob gob, int meshId, String name) {
        if (!FlowerMenuHandler.sendInteractMessage_(gob, meshId)) return false;
        return WaitManager.wait(Constant.Command.Custom.NEW_WIDGET_DID_ADDED, Constant.TimeOut.FREQUENT);
    }

    private static boolean close_() {
        if (widget_ == null) return true;
        if (!sendCloseMessage_()) return false;
        if (WaitManager.wait(Constant.Command.Custom.FLOWER_MENU_CLOSED, Constant.TimeOut.FREQUENT)) {
            clear_();
            return true;
        } else {
            return false;
        }
    }

    private static boolean choose_(String name) {
        if (widget_ == null) return false;
        if (!chooseByName(name)) return false;
        return WaitManager.wait(Constant.Command.Custom.FLOWER_MENU_CLOSED, Constant.TimeOut.FREQUENT);
    }

    private static boolean sendInteractMessage_(haven.Gob gob, int meshId) {
        return WidgetMessageHandler.interact(gob, meshId);
    }

    private static boolean sendChoosePetalMessage_(int index) {
        final int petalCount = widget_.opts.length;
        if (0 <= index && index < petalCount) {
            return WidgetMessageHandler.sendChoosePetalMessage(widget_, index);
        } else {
            return false;
        }
    }

    private static boolean sendCloseMessage_() {
        return WidgetMessageHandler.sendCloseFlowerMenuMessage(widget_);
    }

    private static boolean chooseByName(String name) {
        for (haven.FlowerMenu.Petal petal : widget_.opts) {
            if (petal.name.contentEquals(name)) {
                return sendChoosePetalMessage_(petal.num);
            }
        }

        return false;
    }
}
