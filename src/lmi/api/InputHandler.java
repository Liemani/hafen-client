package lmi.api;

public class InputHandler {
    public static boolean isShiftDown() {
        return lmi.ObjectShadow.ui().modshift;
    }

    public static boolean isControlDown() {
        return lmi.ObjectShadow.ui().modctrl;
    }

    public static boolean isMetaDown() {
        return lmi.ObjectShadow.ui().modmeta;
    }
}
