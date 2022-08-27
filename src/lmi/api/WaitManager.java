package lmi.api;

import lmi.*;

public class WaitManager {
    private static Object monitor_ = new Object();
    private static String command_ = null;
    private static boolean isApplied_ = false;

    // wait
    public static boolean waitTimeOut(String command, long timeOut) {
        init(command);
        return wait_(timeOut);
    }

    // wiat command
    public static boolean waitCommand(String command) {
        init(command);
        if (!wait_()) return false;
        if (!isApplied_) {
            if (!wait_(Constant.TimeOut.TEMPORARY)) return false;
            final boolean isApplied = isApplied_;
            return isApplied;
        }
        return true;
    }

    public static void checkCommandApply(String command) {
        if (!command.contentEquals(command_)) return;
        isApplied_ = true;
        notify_();
        System.out.println("[WaitManager::checkCommandApply() command] " + command);
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

    private static boolean wait_(long timeOut) {
        try {
            synchronized (monitor_) {
                monitor_.wait(timeOut);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }

        return true;
    }

    private static boolean wait_() { return wait_(lmi.Constant.TimeOut.NONE); }

    private static void notify_() {
        synchronized (monitor_) {
            monitor_.notify();
        }
    }
}
