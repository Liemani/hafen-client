package lmi.api;

import lmi.*;

public class WaitManager {
    private static Object monitor_ = new Object();
    private static String command_;

    public static boolean wait(String command, long timeOut) {
        final long startTime = System.currentTimeMillis();

        setCommand_(command);
        startTimer_(timeOut);

        if (!wait_())
            return false;

        final long endTime = System.currentTimeMillis();

        return endTime <= startTime + timeOut;
    }

    public static boolean wait(String command) {
        setCommand_(command);
        return wait_();
    }

    public static void notifyIfCommandEquals(String command) {
        if (!commandEquals_(command))
            return;

        clear_();
        removeTimer_();
        notify_();
    }

    // private methods
    private static void clear_() {
        setCommand_(null);
    }

    private static void setCommand_(String command) {
        command_ = command;
    }

    private static boolean commandEquals_(String command) {
        if (command_ == null || command == null)
            return false;

        return command.contentEquals(command_);
    }

    private static boolean wait_() {
        try {
            synchronized (monitor_) {
                monitor_.wait();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }

        return true;
    }

    private static void notify_() {
        synchronized (monitor_) {
            monitor_.notify();
        }
    }

    // timer
    static Thread timerThread_ = null;
    private static void startTimer_(long timeOut) {
        if (timeOut == Constant.TimeOut.NONE)
            return;

        timerThread_ = new Thread(
            () -> {
                try {
                    Thread.sleep(timeOut);
                } catch (InterruptedException e) {
                    timerThread_ = null;
                    return;
                }

                notify_();
                timerThread_ = null;
            }
        );
        timerThread_.start();
    }

    private static void removeTimer_() {
        if (timerThread_ == null)
            return;

        timerThread_.interrupt();
    }
}
