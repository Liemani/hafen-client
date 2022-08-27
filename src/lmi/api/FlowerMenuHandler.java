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

        if (!open_(gob, meshId)) return false;
        if (choose_(name)) return true;

        System.out.println("[FlowerMenuHandler::choose() flower menu choose failed]");
        if (!close_()) System.out.println("[flower menu close failed]");
        return false;
    }

    // private methods
    private static void clear_() { widget_ = null; }

    // private methods
    private static boolean open_(haven.Gob gob, int meshId) {
        return sendInteractMessage_(gob, meshId);
    }

    private static boolean close_() {
        final boolean succeeded = sendCloseMessage_();
        if (succeeded)
            clear_();
        return succeeded;
    }

    private static boolean choose_(String name) {
        return chooseByName(name);
    }

    private static boolean chooseByName(String name) {
        for (haven.FlowerMenu.Petal petal : widget_.opts) {
            if (petal.name.contentEquals(name)) {
                return sendChoosePetalMessage_(petal.num);
            }
        }

        return false;
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
}
