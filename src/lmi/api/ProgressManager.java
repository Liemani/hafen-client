package lmi.api;

import lmi.*;
import lmi.Constant.*;

public class ProgressManager {
    // status code shadow
    private static final StatusCode SC_SUCCEEDED = StatusCode.SUCCEEDED;
    private static final StatusCode SC_INTERRUPTED = StatusCode.INTERRUPTED;
    private static final StatusCode SC_FAILED = StatusCode.FAILED;
    private static final StatusCode SC_FAILED_OPEN = StatusCode.FAILED_OPEN;

    // command shadow
    private static final Command.Custom C_PROGRESS_DID_STARTED = Command.Custom.PROGRESS_DID_STARTED;
    private static final Command.Custom C_PROGRESS_DID_ENDED = Command.Custom.PROGRESS_DID_ENDED;

    // field
    private static haven.GameUI.Progress widget_ = null;

    // setter
    public static void setWidget(haven.GameUI.Progress widget) { widget_ = widget; }

    // public method
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED_OPEN
    public static StatusCode waitProgress() {
        {
            final StatusCode result =  waitProgressStarting_();
            switch (result) {
                case SUCCEEDED: break;
                case INTERRUPTED: return SC_INTERRUPTED;
                case FAILED_OPEN: return SC_FAILED_OPEN;
                default:
                    new Exception().printStackTrace();
                    return SC_INTERRUPTED;
            }
        }
        while (true) {
            if (waitProgressEnding_() == SC_INTERRUPTED) return SC_INTERRUPTED;
            final StatusCode result = waitProgressStarting_();
            switch (result) {
                case SUCCEEDED: continue;
                case INTERRUPTED: return SC_INTERRUPTED;
                case FAILED: return SC_SUCCEEDED;
                default:
                    new Exception().printStackTrace();
                    return SC_INTERRUPTED;
            }
        }
    }

    // private method
    private static boolean isProgressing_() {
        return widget_ != null;
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED_OPEN
    private static StatusCode waitProgressStarting_() {
        if (isProgressing_()) return SC_SUCCEEDED;
        final StatusCode result = WaitManager.waitTimeOut(C_PROGRESS_DID_STARTED, TimeOut.TEMPORARY);
        switch (result) {
            case SUCCEEDED: return SC_SUCCEEDED;
            case INTERRUPTED: return SC_INTERRUPTED;
            case TIME_OUT: return isProgressing_() ? SC_SUCCEEDED : SC_FAILED_OPEN;
            default:
                new Exception().printStackTrace();
                return SC_INTERRUPTED;
        }
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode waitProgressEnding_() {
        while (true) {
            if (!isProgressing_()) return SC_SUCCEEDED;
            final StatusCode result = WaitManager.waitTimeOut(C_PROGRESS_DID_ENDED, TimeOut.GENERAL);
            switch (result) {
                case SUCCEEDED: return SC_SUCCEEDED;
                case INTERRUPTED: return SC_INTERRUPTED;
                case TIME_OUT: break;
                default:
                    new Exception().printStackTrace();
                    return SC_INTERRUPTED;
            }
        }
    }
}
