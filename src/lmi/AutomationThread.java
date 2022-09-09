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
        try {
            _runnable.run();
        } catch (Exception e) {
            Util.debugPrint(e);
            e.printStackTrace();
        }
        _thread = null;
        System.out.println("[automation is terminating]");
    };

    // package method
    static boolean isRunning() { return _thread != null; }
    static void interrupt() { _thread.interrupt(); }

    static void printStackTrace() {
        if (_thread == null)
            Util.debugPrint("_thread is null");
        else
            _thread.dumpStack();
    }
}
