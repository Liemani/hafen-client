package lmi;

import lmi.Constant.Action;
import static lmi.Constant.ExceptionType.*;
import static lmi.Constant.Action.Custom.*;
import static lmi.Constant.TimeOut.*;

public class ProgressManager {
    // field
    private static haven.GameUI.Progress _widget = null;

    // setter
    public static void setWidget(haven.GameUI.Progress widget) { _widget = widget; }

    // package method
    /// - Throws:
    ///     - ET_PROGRESS_OPEN
    static void waitProgress() {
        ProgressManager._waitProgressAdded();
        while (true) {
            ProgressManager.waitProgressDestroyed();
            try {
                ProgressManager._waitProgressAdded();
            } catch (LMIException e) {
                if (e.type() != ET_PROGRESS_OPEN) throw e;
                break;
            }
        }
    }

    // private method
    private static boolean _isProgressing() { return _widget != null; }

    /// - Throws:
    ///     - ET_PROGRESS_OPEN
    private static void _waitProgressAdded() {
        if (_isProgressing()) return;
        try {
            WaitManager.waitTimeOut(AC_PROGRESS_DID_ADDED, TO_TEMPORARY);
        } catch (LMIException e) {
            if (e.type() != ET_TIME_OUT) throw e;
            if (!_isProgressing()) throw new LMIException(ET_PROGRESS_OPEN);
        }
    }

    private static void waitProgressDestroyed() {
        while (true) {
            if (!_isProgressing()) return;
            try {
                WaitManager.waitTimeOut(AC_PROGRESS_DID_DESTROYED, TO_GENERAL);
                break;
            } catch (LMIException e) {
                if (e.type() != ET_TIME_OUT) throw e;
            }
        }
    }
}
