package lmi;

// import haven
import haven.Gob;
import haven.Coord;

import java.util.Set;
import java.util.TreeMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import lmi.automation.*;

// constant
import static lmi.Constant.ExceptionType.*;
import static lmi.Constant.MeshId.*;
import static lmi.Constant.gfx.borka.*;
import static lmi.Constant.BoundingBox.*;
import static lmi.Constant.*;

class Command {
    // type define
    private static class CommandMap extends TreeMap<String, Method> {};

    // fields
    private static CommandMap _map;

    // automation command
    static Void alignLog() {
        AutomationThread.start(new AlignLog());
        return null;
    }

    static Void dev() {
        AutomationThread.start(new Dev());
        return null;
    }

    // non-command methods
    // all methods with default access modifier will count on as executable command
    public static void init() {
        _map = new CommandMap();
        Method methodArray[] = Command.class.getDeclaredMethods();
        for (Method method : methodArray) {
            if (!Util.methodHasModifier(method, Modifier.PUBLIC)
                    && !Util.methodHasModifier(method, Modifier.PRIVATE)) {
                _map.put(method.getName(), method);
            }
        }
    }

    public static Method getCommandByString(String commandString) {
        return _map.get(commandString);
    }

    public static Set<String> getCommandStringSet() {
        return _map.keySet();
    }
}
