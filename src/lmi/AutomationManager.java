package lmi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.TreeMap;

import static lmi.Constant.TimeOut.*;

import lmi.automation.*;

public class AutomationManager {
    // Type Define
    private static class ClassMap extends TreeMap<String, Class> {};

    // Field
    private static ClassMap _classMap;

    private static Thread _thread;
    private static Runnable _runnable;

    // Getter
    static Class getClass(String name) { return _classMap.get(name); }
    static Set<String> getCommandStringSet() { return _classMap.keySet(); }

    // Initializer
    static void init() {
        final Class[] _classArray = {
            AlignLog.class,
            Dev.class,
        };

        _classMap = new ClassMap();
        for (Class c : _classArray)
            _classMap.put(c.getSimpleName(), c);
    }

    public static void start(Runnable runnable) {
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
        Util.message("[자동화 작업이 종료됐어요]");
    };

    // package method
    static boolean isRunning() { return _thread != null; }
    static void interrupt() { _thread.interrupt(); }

    public static void printStackTrace() {
        if (_thread == null)
            Util.debugPrint("_thread is null");
        else
            _thread.dumpStack();
    }

    static void printCommandStringList(java.io.PrintWriter writer) {
        writer.println("자동화 프로그램 목록:");
        for (String commandString : AutomationManager.getCommandStringSet())
            writer.println("\t" + commandString);
    }
}
