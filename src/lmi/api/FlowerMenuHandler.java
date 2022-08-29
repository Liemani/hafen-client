package lmi.api;

import lmi.*;
import lmi.Constant.StatusCode;
import lmi.Constant.Command;
import lmi.Constant.TimeOut;

public class FlowerMenuHandler {
    // status code shadow
    private static final StatusCode SC_SUCCEEDED = StatusCode.SUCCEEDED;
    private static final StatusCode SC_INTERRUPTED = StatusCode.INTERRUPTED;
    private static final StatusCode SC_INVALID_ARGUMENT = StatusCode.INVALID_ARGUMENT;
    private static final StatusCode SC_NO_MATCHING = StatusCode.NO_MATCHING;

    // command shadow
    private static final Command.Custom C_PROGRESS_DID_ADDED = Command.Custom.PROGRESS_DID_ADDED;
    private static final Command.Custom C_PROGRESS_DID_DESTROYED = Command.Custom.PROGRESS_DID_DESTROYED;

    // assume widget_ never collision
    private static haven.FlowerMenu widget_;

    // set widget
    public static void setWidget(haven.FlowerMenu widget) { widget_ = widget; }

    // choose
    // TODO progress can end before wait it
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_INVALID_ARGUMENT
    ///     - SC_NO_MATCHING
    public static StatusCode choose(haven.Gob gob, int meshId, String name) {
        if (gob == null || name == null)
            return SC_INVALID_ARGUMENT;

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
    private static StatusCode open_(haven.Gob gob, int meshId) {
        return sendInteractMessage_(gob, meshId);
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
    ///     - SC_NO_MATCHING
    private static StatusCode choose_(String name) {
        for (haven.FlowerMenu.Petal petal : widget_.opts)
            if (petal.name.contentEquals(name))
                return sendChoosePetalMessage_(petal.num);
        return SC_NO_MATCHING;
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
    ///     - SC_NO_MATCHING
    private static StatusCode sendChoosePetalMessage_(int index) {
        final int petalCount = widget_.opts.length;
        if (0 <= index && index < petalCount) {
            return WidgetMessageHandler.sendChoosePetalMessage(widget_, index);
        } else {
            return SC_NO_MATCHING;
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
