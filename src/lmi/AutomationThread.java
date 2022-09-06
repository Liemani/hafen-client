package lmi;

import static lmi.Constant.TimeOut.*;

class AutomationThread {
    private static Thread _thread;
    private static Runnable _runnable;

    static void start(Runnable runnable) {
        if (_thread != null)
            _thread.interrupt();

        try {
            while (_thread != null)
                Thread.sleep(TO_GENERAL);
        } catch (InterruptedException e) {
            System.out.println("thread interrupted before run");
            return;
        }

        _runnable = runnable;
        _thread = new Thread(_mainRunnable);
        _thread.start();
    }

    // main runnable
    private static final Runnable _mainRunnable = () -> {
        _runnable.run();
        _thread = null;
        System.out.println("[automation is terminating]");
    };

    // package method
    static void interrupt() { _thread.interrupt(); }
    static void printStackTrace() { _thread.dumpStack(); }
}
