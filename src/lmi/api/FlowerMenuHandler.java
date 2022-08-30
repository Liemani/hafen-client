package lmi.api;

import lmi.*;
import lmi.Constant.*;

public class FlowerMenuHandler {
    // status code shadow
    private static final StatusCode SC_SUCCEEDED = StatusCode.SUCCEEDED;
    private static final StatusCode SC_INTERRUPTED = StatusCode.INTERRUPTED;
    private static final StatusCode SC_FAILED_INVALID_ARGUMENT = StatusCode.FAILED_INVALID_ARGUMENT;
    private static final StatusCode SC_FAILED = StatusCode.FAILED;
    private static final StatusCode SC_TIME_OUT = StatusCode.TIME_OUT;
    private static final StatusCode SC_FAILED_MATCH = StatusCode.FAILED_MATCH;
    private static final StatusCode SC_FAILED_OPEN = StatusCode.FAILED_OPEN;

    // command shadow
    private static final Command.Custom C_FLOWER_MENU_DID_ADDED = Command.Custom.FLOWER_MENU_DID_ADDED;

    // command shadow
    private static final Command.Custom C_PROGRESS_DID_ADDED = Command.Custom.PROGRESS_DID_ADDED;
    private static final Command.Custom C_PROGRESS_DID_DESTROYED = Command.Custom.PROGRESS_DID_DESTROYED;

    // field
    private static haven.FlowerMenu widget_;

    // set widget
    public static void setWidget(haven.FlowerMenu widget) { widget_ = widget; }

    // choose
    // TODO progress can end before wait it
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED_INVALID_ARGUMENT
    ///     - SC_FAILED_OPEN
    ///     - SC_FAILED_MATCH
    public static StatusCode choose(haven.Gob gob, int meshId, String name) {
        if (gob == null || name == null)
            return SC_FAILED_INVALID_ARGUMENT;

        {
            final StatusCode result = open_(gob, meshId);
            if (result != SC_SUCCEEDED) return result;
        }
        {
            final StatusCode result = choose_(name);
            if (result != SC_SUCCEEDED) {
                close_();
                return result;
            }
        }
        return waitEnd_();
    }

    // private methods
    private static void clear_() { widget_ = null; }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED_OPEN
    private static StatusCode open_(haven.Gob gob, int meshId) {
        if (sendInteractMessage_(gob, meshId) == SC_INTERRUPTED) return SC_INTERRUPTED;
        return waitFlowerMenuOpening();
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED_OPEN
    private static StatusCode waitFlowerMenuOpening() {
        final StatusCode result = WaitManager.waitTimeOut(C_FLOWER_MENU_DID_ADDED, TimeOut.TEMPORARY);
        if (result != SC_TIME_OUT) return result;
        if (isAdded_())
            return SC_SUCCEEDED;
        else
            return SC_FAILED_OPEN;
    }

    private static boolean isAdded_() {
        return widget_ != null;
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode close_() {
        final StatusCode result = sendCloseMessage_();
        if (result == SC_SUCCEEDED)
            clear_();
        return result;
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED_MATCH
    private static StatusCode choose_(String name) {
        for (haven.FlowerMenu.Petal petal : widget_.opts)
            if (petal.name.contentEquals(name))
                return sendChoosePetalMessage_(petal.num);
        return SC_FAILED_MATCH;
    }

    // TODO progress can added before wait it
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode waitEnd_() {
        while (true) {
            {
                final StatusCode result = WaitManager.waitCommand(C_PROGRESS_DID_DESTROYED);
                if (result != SC_SUCCEEDED) return result;
            }
            {
                final StatusCode result = WaitManager.waitTimeOut(C_PROGRESS_DID_ADDED, TimeOut.TEMPORARY);
                switch (result) {
                    case SUCCEEDED:
                        break;
                    case INTERRUPTED:
                        return SC_INTERRUPTED;
                    case TIME_OUT:
                        return SC_SUCCEEDED;
                    default:
                        new Exception().printStackTrace();
                        return SC_INTERRUPTED;
                }
            }
        }
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED_MATCH
    private static StatusCode sendChoosePetalMessage_(int index) {
        final int petalCount = widget_.opts.length;
        if (0 <= index && index < petalCount) {
            return WidgetMessageHandler.sendChoosePetalMessage(widget_, index);
        } else {
            return SC_FAILED_MATCH;
        }
    }

    // send message shadow
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode sendInteractMessage_(haven.Gob gob, int meshId) {
        return WidgetMessageHandler.interact(gob, meshId);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode sendCloseMessage_() {
        return WidgetMessageHandler.sendCloseFlowerMenuMessage(widget_);
    }
}
