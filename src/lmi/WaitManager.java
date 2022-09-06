package lmi;

import lmi.Constant.Action;
import static lmi.Constant.ExceptionType.*;
import static lmi.Constant.Action.Custom.*;
import static lmi.Constant.TimeOut.*;

public class WaitManager {
    // property
    private static Object _subject = null;
    private static String _action = null;
    private static Action.Custom _customAction = AC_NONE;

    // notify
    public static void notifyAction(Object subject, String action) {
        synchronized (WaitManager.class) {
            if (!_equals(subject, action)) return;

            _notify();
        }
        lmi.Util.debugPrint("action: " + action);
    }

    public static void notifyAction(Object subject, Action.Custom customAction) {
        synchronized (WaitManager.class) {
            if (!_equals(subject, customAction)) return;

            _notify();
        }
        lmi.Util.debugPrint("custom action: " + customAction);
    }

    public static void notifyAction(String action) {
        notifyAction(null, action);
    }

    public static void notifyAction(Action.Custom customAction) {
        notifyAction(null, customAction);
    }

    // wait
    public static void waitAction(String action) {
        _action = action;
        _wait();
    }

    public static void waitAction(Action.Custom customAction) {
        _customAction = customAction;
        _wait();
    }

    /// - Throws:
    ///     - ET_TIME_OUT
    public static void waitTimeOut(Object subject, String action, long timeOut) {
        _init(subject, action);
        _waitTimeOut(timeOut);
    }

    public static void waitTimeOut(String action, long timeOut) {
        WaitManager.waitTimeOut(null, action, timeOut);
    }

    public static void waitTimeOut(Object subject, Action.Custom customAction, long timeOut) {
        _init(subject, customAction);
        _waitTimeOut(timeOut);
    }

    public static void waitTimeOut(Action.Custom customAction, long timeOut) {
        WaitManager.waitTimeOut(null, customAction, timeOut);
    }

    // private methods
    /// - Throws:
    ///     - ET_TIME_OUT
    private static void _waitTimeOut(long timeOut) {
        final long startTime = System.currentTimeMillis();
        _wait(timeOut);
        final long endTime = System.currentTimeMillis();

        if (endTime - startTime >= timeOut)
            throw new LMIException(ET_TIME_OUT);
    }

    private static void _init(Object subject, String action) {
        _subject = subject;
        _action = action;
    }

    private static void _init(Object subject, Action.Custom customAction) {
        _subject = subject;
        _customAction = customAction;
    }

    // clear
    private static void _clear() {
        _subject = null;
        _action = null;
        _customAction = AC_NONE;
    }

    // equal
    private static boolean _equals(Object subject, String action) {
        return _subjectEquals(subject) && _actionEquals(action);
    }

    private static boolean _equals(Object subject, Action.Custom customAction) {
        return _subjectEquals(subject) && _actionEquals(customAction);
    }

    private static boolean _subjectEquals(Object subject) { return subject == _subject; }

    private static boolean _actionEquals(String action) {
        if (_action == null) return false;
        return action.contentEquals(_action);
    }

    private static boolean _actionEquals(Action.Custom customAction) { return customAction == _customAction; }

    // wrap wait
    private static void _wait(long timeOut) {
        synchronized (WaitManager.class) {
            try {
                WaitManager.class.wait(timeOut);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new LMIException(ET_INTERRUPTED);
            } finally { _clear(); }
        }
    }

    private static void _wait() { _wait(TO_NONE); }

    // wrap notify
    private static void _notify() {
        WaitManager.class.notify();
    }
}
