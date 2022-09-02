package lmi.api;

import lmi.*;
import lmi.Constant.StatusCode;
import lmi.Constant.Action;

import static lmi.Constant.StatusCode.*;
import static lmi.Constant.Action.Custom.*;
import static lmi.Constant.TimeOut.*;

public class WaitManager {
    // property
    private static Object subject_ = null;
    private static String action_ = null;
    private static Action.Custom customAction_ = AC_NONE;

    // notify
    public static void notifyAction(Object subject, String action) {
        if (action_ == null || action == null) return;
        if (!equals_(subject, action)) return;

        notify_();
        lmi.Util.debugPrint(WaitManager.class, "action: " + action);
    }

    public static void notifyAction(Object subject, Action.Custom customAction) {
        if (!equals_(subject, customAction)) return;

        notify_();
        lmi.Util.debugPrint(WaitManager.class, "custom action: " + customAction);
    }

    public static void notifyAction(String action) {
        notifyAction(null, action);
    }

    public static void notifyAction(Action.Custom customAction) {
        notifyAction(null, customAction);
    }

    // wait
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    public static StatusCode waitAction(String action) {
        action_ = action;
        return wait_();
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_TIME_OUT
    public static StatusCode waitTimeOut(Object subject, String action, long timeOut) {
        init_(subject, action);
        return waitTimeOut_(timeOut);
    }

    public static StatusCode waitTimeOut(Object subject, Action.Custom customAction, long timeOut) {
        init_(subject, customAction);
        return waitTimeOut_(timeOut);
    }

    public static StatusCode waitTimeOut(String action, long timeOut) {
        return waitTimeOut(null, action, timeOut);
    }

    public static StatusCode waitTimeOut(Action.Custom customAction, long timeOut) {
        return waitTimeOut(null, customAction, timeOut);
    }

    // private methods
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_TIME_OUT
    private static StatusCode waitTimeOut_(long timeOut) {
        final long startTime = System.currentTimeMillis();
        if (wait_(timeOut) == SC_INTERRUPTED) return SC_INTERRUPTED;
        final long endTime = System.currentTimeMillis();
        if (startTime + timeOut >= endTime)
            return SC_SUCCEEDED;
        else
            return SC_TIME_OUT;
    }

    private static void init_(Object subject, String action) {
        subject_ = subject;
        action_ = action;
    }

    private static void init_(Object subject, Action.Custom customAction) {
        subject_ = subject;
        customAction_ = customAction;
    }

    // clear
    private static void clear_() {
        subject_ = null;
        action_ = null;
        customAction_ = AC_NONE;
    }

    // equal
    private static boolean equals_(Object subject, String action) {
        return subjectEquals_(subject) && actionEquals_(action);
    }

    private static boolean equals_(Object subject, Action.Custom customAction) {
        return subjectEquals_(subject) && actionEquals_(customAction);
    }

    private static boolean subjectEquals_(Object subject) { return subject == subject_; }
    private static boolean actionEquals_(String action) { return action.contentEquals(action_); }
    private static boolean actionEquals_(Action.Custom customAction) { return customAction == customAction_; }

    // wrap wait
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode wait_(long timeOut) {
        try {
            synchronized (WaitManager.class) {
                WaitManager.class.wait(timeOut);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            clear_();
            return SC_INTERRUPTED;
        }
        clear_();
        return SC_SUCCEEDED;
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode wait_() { return wait_(TO_NONE); }

    // wrap notify
    private static void notify_() {
        synchronized (WaitManager.class) {
            WaitManager.class.notify();
        }
    }
}
