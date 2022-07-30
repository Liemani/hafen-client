package lmi;

import java.util.Set;
import java.util.TreeMap;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

class Command {
    // type define
    private static class CommandMap extends TreeMap<String, Method> {};

    // fields
    private static lmi.MainThread mainThread_;
    private static lmi.Command.CommandMap map_;

    // commands
    static Void hello() {
        System.out.println("Hello, world!");

        return null;
    }

    static Void printUIPanelClassName() {
        System.out.println("class name of uiPanel: " + mainThread_.uiPanel_.getClass().getName());

        return null;
    }

    static Void printListOfMainFrame() {
        mainThread_.mainFrame_.list(System.out, 4);

        return null;
    }

    // package method
    /// assume: each of all non-public methods are method for command
    /// add all private methods to map_
    public static void init(lmi.MainThread mainThread) {
        mainThread_ = mainThread;

        map_ = new CommandMap();
        Method methodArray[] = Command.class.getDeclaredMethods();
        for (Method method : methodArray) {
            if ((method.getModifiers() & Modifier.PUBLIC) == 0) {
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
