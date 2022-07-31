package lmi;

import java.util.Set;
import java.util.TreeMap;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

class Command {
    // type define
    private static class CommandMap extends TreeMap<String, Method> {};

    // fields
    private static CommandMap map_;

    // commands
    static Void commandTemplate() {
        // code here...

        return null;
    }

    static Void printObjectShadow() {
        lmi.debug.Debug.debugDescribe(System.out, lmi.ObjectShadow.class);

        return null;
    }

    static Void printListOfMainFrame() {
        lmi.ObjectShadow.mainFrame_.list(System.out, 4);

        return null;
    }

    static Void exit() {
        interruptHavenMainThread();

        return null;
    }

    // private commands
    private static Void interruptHavenMainThread() {
        lmi.ObjectShadow.mainThread_.interrupt();

        return null;
    }

    // package method
    /// all methods with default access modifier will count on as executable command
    public static void init() {
        map_ = new CommandMap();
        Method methodArray[] = Command.class.getDeclaredMethods();
        for (Method method : methodArray) {
            if (!lmi.Util.methodHasModifier(method, Modifier.PUBLIC)
                    && !lmi.Util.methodHasModifier(method, Modifier.PRIVATE)) {
                map_.put(method.getName(), method);
            }
        }
    }

    public static Method getCommandByString(String commandString) {
        return map_.get(commandString);
    }

    public static Set<String> getCommandStringSet() {
        return map_.keySet();
    }
}
