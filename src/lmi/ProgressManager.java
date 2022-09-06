package lmi;

import lmi.Constant.StatusCode;
import static lmi.Constant.StatusCode.*;
import lmi.Constant.Action;
import static lmi.Constant.Action.Custom.*;
import static lmi.Constant.TimeOut.*;

public class ProgressManager {
    // field
    private static haven.GameUI.Progress widget_ = null;

    // setter
    public static void setWidget(haven.GameUI.Progress widget) { widget_ = widget; }

    // package method
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED_OPEN_PROGRESS
    static StatusCode waitProgress() {
        {
            final StatusCode result =  waitProgressAdded_();
            switch (result) {
                case SC_SUCCEEDED: break;
                case SC_INTERRUPTED: return SC_INTERRUPTED;
                case SC_FAILED_OPEN_PROGRESS: return SC_FAILED_OPEN_PROGRESS;
                default:
                    new Exception().printStackTrace();
                    return SC_INTERRUPTED;
            }
        }
        while (true) {
            if (waitProgressDestroyed() == SC_INTERRUPTED) return SC_INTERRUPTED;
            final StatusCode result = waitProgressAdded_();
            switch (result) {
                case SC_SUCCEEDED: continue;
                case SC_INTERRUPTED: return SC_INTERRUPTED;
                case SC_FAILED_OPEN_PROGRESS: return SC_SUCCEEDED;
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
    ///     - SC_FAILED_OPEN_PROGRESS
    private static StatusCode waitProgressAdded_() {
        if (isProgressing_()) return SC_SUCCEEDED;
        final StatusCode result = WaitManager.waitTimeOut(AC_PROGRESS_DID_ADDED, TO_TEMPORARY);
        switch (result) {
            case SC_SUCCEEDED: return SC_SUCCEEDED;
            case SC_INTERRUPTED: return SC_INTERRUPTED;
            case SC_TIME_OUT: return isProgressing_() ? SC_SUCCEEDED : SC_FAILED_OPEN_PROGRESS;
            default:
                new Exception().printStackTrace();
                return SC_INTERRUPTED;
        }
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode waitProgressDestroyed() {
        while (true) {
            if (!isProgressing_()) return SC_SUCCEEDED;
            final StatusCode result = WaitManager.waitTimeOut(AC_PROGRESS_DID_DESTROYED, TO_GENERAL);
            switch (result) {
                case SC_SUCCEEDED: return SC_SUCCEEDED;
                case SC_INTERRUPTED: return SC_INTERRUPTED;
                case SC_TIME_OUT: break;
                default:
                    new Exception().printStackTrace();
                    return SC_INTERRUPTED;
            }
        }
    }
}
