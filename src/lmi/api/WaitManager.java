package lmi.api;

import lmi.*;

public class WaitManager {
    // status code shadow
    private static final Constant.StatusCode SC_SUCCEEDED = Constant.StatusCode.SUCCEEDED;
    private static final Constant.StatusCode SC_INTERRUPTED = Constant.StatusCode.INTERRUPTED;
    private static final Constant.StatusCode SC_FAILED = Constant.StatusCode.FAILED;
    private static final Constant.StatusCode SC_TIME_OUT = Constant.StatusCode.TIME_OUT;

    private static Object monitor_ = new Object();
    private static String command_ = null;
    private static boolean isApplied_ = false;

    // wait
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_TIME_OUT
    public static Constant.StatusCode waitTimeOut(String command, long timeOut) {
        init(command);
        final long startTime = System.currentTimeMillis();
        {
            final Constant.StatusCode result = wait_();
            if (result != SC_SUCCEEDED) return result;
        }
        final long endTime = System.currentTimeMillis();
        if (startTime + timeOut >= endTime)
            return SC_SUCCEEDED;
        else
            return SC_TIME_OUT;
    }

    // wiat command
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED
    public static Constant.StatusCode waitCommand(String command) {
        init(command);
        {
            final Constant.StatusCode result = wait_();
            if (result != SC_SUCCEEDED) return result;
        }
        if (!isApplied_) {
            {
                final Constant.StatusCode result = wait_(Constant.TimeOut.TEMPORARY);
                if (result != SC_SUCCEEDED) return result;
            }
            lmi.Util.debugPrint(WaitManager.class, "isApplied_: " + isApplied_);
            if (isApplied_)
                return SC_SUCCEEDED;
            else
                return SC_FAILED;
        }
        return SC_SUCCEEDED;
    }

    public static void checkCommandApply(String command) {
        if (!command.contentEquals(command_)) return;
        isApplied_ = true;
        notify_();
        lmi.Util.debugPrint(WaitManager.class, "command: " + command);
    }

    public static void checkACKCommand(String command) {
        if (command_ == null || command == null) return;
        if (!commandEquals_(command)) return;
        notify_();
    }

    // private methods
    private static void clear_() {
        command_ = null;
        isApplied_ = false;
    }

    private static void init(String command) {
        clear_();
        command_ = command;
    }

    private static boolean commandEquals_(String command) {
        return command.contentEquals(command_);
    }

    // wrap wait
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static Constant.StatusCode wait_(long timeOut) {
        try {
            synchronized (monitor_) {
                monitor_.wait(timeOut);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return SC_INTERRUPTED;
        }
        return SC_SUCCEEDED;
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static Constant.StatusCode wait_() { return wait_(lmi.Constant.TimeOut.NONE); }

    // wrap notify
    private static void notify_() {
        synchronized (monitor_) {
            monitor_.notify();
        }
    }
}
