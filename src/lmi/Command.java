package lmi;

import java.util.Set;
import java.util.TreeMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;


class Command {
    static Thread macroThread_ = null;

    // type define
    private static class CommandMap extends TreeMap<String, Method> {};

    // fields
    private static CommandMap map_;

    // test command
    static Void test() {
        lmi.Debug.debugDescribeField(lmi.macro.Player.gob());
        lmi.Debug.debugDescribeField(lmi.macro.Player.gob().getc());
        lmi.Debug.debugDescribeField(lmi.macro.Player.gob().getv());
        return null;
    }

    // macro command
    static Void macroInterrupte() {
        macroThread_.interrupt();
        return null;
    }

    static Void macroConnect() {
        macroThread_ = new Thread(new lmi.macro.Connect());
        macroThread_.start();
        return null;
    }

    static Void macroPatrol000() {
        macroThread_ = new Thread(new lmi.macro.Patrol000());
        macroThread_.start();
        return null;
    }

    static Void macroPatrol001() {
        macroThread_ = new Thread(new lmi.macro.Patrol001());
        macroThread_.start();
        return null;
    }

    static Void macroPatrol002() {
        macroThread_ = new Thread(new lmi.macro.Patrol002());
        macroThread_.start();
        return null;
    }

    static Void macroTest() {
        macroThread_ = new Thread(new lmi.macro.Test());
        macroThread_.start();
        return null;
    }

    static Void macroDescribeLastClickedGob() {
        lmi.macro.Util.describeLastClickedGob();
        return null;
    }

    // action command
    static Void dig() {
        lmi.macro.Self.dig();
        return null;
    }

    // etc command
    static Void playerDescribe() {
        lmi.macro.Self.debugDescribe();
        return null;
    }

    static Void printObjectShadow() {
        Debug.debugDescribeField(System.out, lmi.ObjectShadow.class);
        return null;
    }

    static Void recordMouseLocation() {
        lmi.macro.AWTEventGenerator.setMouseLocation(ObjectShadow.ui_.mc.x, ObjectShadow.ui_.mc.y);
        return null;
    }

    static Void printRecordedMouseLocation() {
        lmi.macro.AWTEventGenerator.printRecordedMouseLocation();
        return null;
    }

//      // awt commands
//      static Void awtGenerateMouseClick() {
//          lmi.macro.AWTEventGenerator.generateMouseClickGeneral(java.awt.event.MouseEvent.BUTTON3);
//          return null;
//      }
//  
//      static Void awtGenerateMouseRightClick() {
//          lmi.macro.AWTEventGenerator.generateMouseClickModified(0, java.awt.event.MouseEvent.BUTTON3);
//          return null;
//      }
//  
//      static Void toggleEquipment() {
//          lmi.macro.AWTEventGenerator.generateCtrlE();
//          return null;
//      }
//  
//      static Void openAxeCraftWindow() {
//          lmi.macro.AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_C);
//          lmi.macro.AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_C);
//          lmi.macro.AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_T);
//          lmi.macro.AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_A);
//          lmi.macro.AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_T);
//          return null;
//      }
//  
//      static Void typeEnter() {
//          lmi.macro.AWTEventGenerator.generateKeyPushUpSpecialKey(java.awt.event.KeyEvent.VK_ENTER);
//          return null;
//      }
//  
//      static Void typeTab() {
//          lmi.macro.AWTEventGenerator.generateKeyPushUpSpecialKey(java.awt.event.KeyEvent.VK_TAB);
//          return null;
//      }

    // Wrapping ObjectFinder
    static Void objectInit() {
        wrapObjectFinderFind(Util.MemberType.FIELD, ObjectShadow.class, true);
        return null;
    }

    static Void objectInitByLastClickedGob() {
        ObjectFinder.init();
        ObjectFinder.moveForward(lmi.macro.Util.lastClickedGob());
        return null;
    }

    static Void objectChange() {
        wrapObjectFinderFind(Util.MemberType.FIELD, null, true);
        return null;
    }

    static Void objectUndo() {
        if (ObjectFinder.isEmpty()) {
            System.out.println("there is no previous object");
            return null;
        }

        ObjectFinder.moveBackward();
        Debug.debugDescribeField(ObjectFinder.last());
        return null;
    }

    static Void objectChangeToReturnValueOfMethod() {
        wrapObjectFinderFind(Util.MemberType.METHOD, null, true);
        return null;
    }

    static Void objectDescribe() {
        Debug.debugDescribeField(ObjectFinder.last());
        return null;
    }

    static Void objectDescribeAsIterable() {
        Object object = ObjectFinder.last();
        if (!(object instanceof Iterable)) {
            System.out.println(Debug.convertToDebugDescriptionClassNameHashCode(object) + " is not instance of Iterable");
            return null;
        }

        System.out.println("[" + Debug.convertToDebugDescriptionClassNameHashCode(object) + "]");
        for (Object element : (Iterable)object) {
            Debug.debugDescribeField(element);
        }
        return null;
    }

    private static void wrapObjectFinderFind(Util.MemberType type, Class classObjectToReset, boolean willAppend) {
        Debug.debugDescribeClassNameHashCodeWithTag("current: ", ObjectFinder.last());

        Object object = null;
        try {
            object = ObjectFinder.find(type, classObjectToReset);
            Debug.debugDescribeField(object);
            if (willAppend) {
                if (!type.isField())
                    ObjectFinder.init();
                ObjectFinder.moveForward(object);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    static Void objectListAsWidget() {
        ObjectFinder.listLastAsWidget();
        return null;
    }

    // non-command methods
    // all methods with default access modifier will count on as executable command
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
