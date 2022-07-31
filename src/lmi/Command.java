package lmi;

import java.util.Set;
import java.util.TreeMap;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

class Command {
    // type define
    private static class CommandMap extends TreeMap<String, Method> {};

    // fields
    private static lmi.Command.CommandMap map_;

    // commands
    static Void emptyCommandTemplate() {
        // code here...

        return null;
    }

    static Void printObjectShadow() {
        lmi.debug.Debug.debugDescribe(System.out, lmi.ObjectShadow.class);

        return null;
    }

    static Void printUIPanelClassName() {
        System.out.println("class name of uiPanel: " + lmi.ObjectShadow.uiPanel_.getClass().getName());

        return null;
    }

    static Void printListOfMainFrame() {
        lmi.ObjectShadow.mainFrame_.list(System.out, 4);

        return null;
    }

    static Void interruptMainHavenThread() {
        try {
            lmi.ObjectShadow.mainThread_.interrupt();
        }
        catch (Exception e) {
            System.out.println("<access denied>");
        }

        return null;
    }

    // package method
    /// assume: each of all non-public methods are method for command
    /// add all private methods to map_
    public static void init() {
        map_ = new CommandMap();
        Method methodArray[] = Command.class.getDeclaredMethods();
        for (Method method : methodArray) {
            if (!lmi.Util.methodHasModifier(method, Modifier.PUBLIC)) {
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
