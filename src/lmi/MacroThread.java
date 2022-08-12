package lmi;

class MacroThread {
    static Thread thread_;

    static void start(Runnable runnable) {
        if (thread_ != null)
            thread_.interrupt();
        try {
            while (thread_ != null);
                Thread.sleep(Constant.Time.GENERAL_SLEEP);
        } catch (InterruptedException e) { System.out.println("thread interrupted before run"); }
        thread_ = new Thread(() -> {
                runnable.run();
                thread_ = null;
                System.out.println("[macro is terminating]");
                });
        thread_.start();
    }

    static void interrupt() {
        thread_.interrupt();
    }
}
