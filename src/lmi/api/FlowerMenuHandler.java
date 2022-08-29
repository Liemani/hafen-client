package lmi.api;

import lmi.*;

public class FlowerMenuHandler {
    // status code shadow
    private static final Constant.StatusCode SC_SUCCEEDED = Constant.StatusCode.SUCCEEDED;
    private static final Constant.StatusCode SC_INTERRUPTED = Constant.StatusCode.INTERRUPTED;
    private static final Constant.StatusCode SC_INVALID_ARGUMENT = Constant.StatusCode.INVALID_ARGUMENT;
    private static final Constant.StatusCode SC_NO_MATCHING = Constant.StatusCode.NO_MATCHING;

    // command shadow
    private static final String C_PROGRESS_DID_ADDED = Constant.Command.Custom.PROGRESS_DID_ADDED;
    private static final String C_PROGRESS_DID_DESTROYED = Constant.Command.Custom.PROGRESS_DID_DESTROYED;

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
    public static Constant.StatusCode choose(haven.Gob gob, int meshId, String name) {
        if (gob == null || name == null)
            return SC_INVALID_ARGUMENT;

        {
            final Constant.StatusCode result = open_(gob, meshId);
            if (result != SC_SUCCEEDED) return result;
        }
        {
            final Constant.StatusCode result = choose_(name);
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
    private static Constant.StatusCode open_(haven.Gob gob, int meshId) {
        return sendInteractMessage_(gob, meshId);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static Constant.StatusCode close_() {
        final Constant.StatusCode result = sendCloseMessage_();
        if (result == SC_SUCCEEDED)
            clear_();
        return result;
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_NO_MATCHING
    private static Constant.StatusCode choose_(String name) {
        for (haven.FlowerMenu.Petal petal : widget_.opts)
            if (petal.name.contentEquals(name))
                return sendChoosePetalMessage_(petal.num);
        return SC_NO_MATCHING;
    }

    // TODO progress can added before wait it
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static Constant.StatusCode waitEnd_() {
        while (true) {
            {
                final Constant.StatusCode result = WaitManager.waitCommand(C_PROGRESS_DID_DESTROYED);
                if (result != SC_SUCCEEDED) return result;
            }
            {
                final Constant.StatusCode result = WaitManager.waitTimeOut(C_PROGRESS_DID_ADDED, Constant.TimeOut.TEMPORARY);
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
    private static Constant.StatusCode sendChoosePetalMessage_(int index) {
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
    private static Constant.StatusCode sendInteractMessage_(haven.Gob gob, int meshId) {
        return WidgetMessageHandler.interact(gob, meshId);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static Constant.StatusCode sendCloseMessage_() {
        return WidgetMessageHandler.sendCloseFlowerMenuMessage(widget_);
    }
}
