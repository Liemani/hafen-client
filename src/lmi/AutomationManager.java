package lmi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.TreeMap;

import static lmi.Constant.TimeOut.*;
import static lmi.Constant.ExceptionType.*;

import lmi.automation.*;

public class AutomationManager {
    // Type Define
    private static class AutomationMap extends TreeMap<String, Class<Automation>> {};

    // Field
    private static AutomationMap _automationMap;

    private static Thread _thread;
    private static Runnable _runnable;

    // Getter
    static Class<Automation> getClass(String name) { return _automationMap.get(name); }
    static Set<String> getCommandStringSet() { return _automationMap.keySet(); }

    // Initializer
    @SuppressWarnings("unchecked")
    static void init() {
        final Class[] _classArray = {
            AlignLog.class,
        };

        _automationMap = new AutomationMap();
        for (Class c : _classArray)
            if (Automation.class.isAssignableFrom(c))
                _automationMap.put(c.getSimpleName(), (Class<Automation>)c);
    }

    public static void start(Class<Automation> automationClass, String[] args) throws Exception {
        if (_thread != null)
            _thread.interrupt();

        while (_thread != null)
            Thread.sleep(TO_GENERAL);

        Automation automation;
        try {
            automation = automationClass.newInstance();
        } catch (Exception e) {
            if (!(e instanceof IllegalAccessException)) throw e;
            throw new LMIException(ET_COMMAND_INITIALIZER);
        }
        automation.setArgs(args);
        _runnable = automation;
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

    public static class Automation implements Runnable{
        protected String[] _args;
        final void setArgs(String[] args) { _args = args; }

        // Runnable Requirement
        public void run() {}
    }
}
