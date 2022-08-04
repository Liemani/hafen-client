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
        Debug.debugDescribe(System.out, lmi.ObjectShadow.class);

        return null;
    }

    static Void printListOfMainFrame() {
        lmi.ObjectShadow.mainFrame_.list(System.out, 4);

        return null;
    }

    static Void printMainFrame() {
        Debug.debugDescribe(ObjectShadow.mainFrame_);

        return null;
    }

    static Void printJOGLPanel() {
        Debug.debugDescribe(ObjectShadow.joglPanel_);

        return null;
    }

    static Void printDispatcher() {
        Debug.debugDescribe(ObjectShadow.dispatcher_);

        return null;
    }

    static Void printUIRunner() {
        Debug.debugDescribe(ObjectShadow.uiRunner_);

        return null;
    }

    static Void printUI() {
        Debug.debugDescribe(ObjectShadow.ui_);

        return null;
    }

    static Void printRootWidget() {
        Debug.debugDescribe(ObjectShadow.rootWidget_);

        return null;
    }

    static Void recordMouseLocation() {
        AWTEventGenerator.setMouseLocation(ObjectShadow.ui_.mc.x, ObjectShadow.ui_.mc.y);

        return null;
    }

    static Void printRecordedMouseLocation() {
        Debug.debugDescribe(AWTEventGenerator.class);

        return null;
    }

    static Void generateMouseClick() {
        AWTEventGenerator.generateMouseClickGeneral(java.awt.event.MouseEvent.BUTTON3);

        return null;
    }

    static Void generateMouseRightClick() {
        AWTEventGenerator.generateMouseClickModified(0, java.awt.event.MouseEvent.BUTTON3);

        return null;
    }

    static Void toggleEquipment() {
        AWTEventGenerator.generateCtrlE();

        return null;
    }

    static Void openAxeCraftWindow() {
        AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_C);
        AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_C);
        AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_T);
        AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_A);
        AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_T);

        return null;
    }

    static Void typeEnter() {
        AWTEventGenerator.generateKeyPushUpSpecialKey(java.awt.event.KeyEvent.VK_ENTER);

        return null;
    }

    static Void typeTab() {
        AWTEventGenerator.generateKeyPushUpSpecialKey(java.awt.event.KeyEvent.VK_TAB);

        return null;
    }

    static Void exit() {
        System.out.println("[terminating]");
        interruptHavenMainThread();

        return null;
    }

    static haven.Widget currentWidget_ = null;
    static Void resetCurrentWidgetAsRoot() {
        currentWidget_ = ObjectShadow.rootWidget_;

        return null;
    }

    static Void printCurrentWidgetClassName() {
        System.out.println(currentWidget_);

        return null;
    }

    static Void moveNextWidget() {
        if (currentWidget_.next != null) {
            currentWidget_ = currentWidget_.next;
            printCurrentWidgetClassName();
        }
        else
            System.out.println("next widget is null");

        return null;
    }

    static Void movePreviousWidget() {
        if (currentWidget_.prev != null) {
            currentWidget_ = currentWidget_.prev;
            printCurrentWidgetClassName();
        }
        else
            System.out.println("previous widget is null");

        return null;
    }

    static Void moveChildWidget() {
        if (currentWidget_.child != null) {
            currentWidget_ = currentWidget_.child;
            printCurrentWidgetClassName();
        }
        else
            System.out.println("child widget is null");

        return null;
    }

    static Void moveParentWidget() {
        if (currentWidget_.parent != null) {
            currentWidget_ = currentWidget_.parent;
            printCurrentWidgetClassName();
        }
        else
            System.out.println("parent widget is null");

        return null;
    }

    static Void iterateWidget() {
        iterateWidget(currentWidget_, 0);

        return null;
    }

    private static void iterateWidget(haven.Widget widget, int indentCount) {
        haven.Widget child = widget.child;

        for (; child != null; child = child.next) {
            insertIndent(indentCount);
            System.out.println(child.getClass().getName());
            iterateWidget(child, indentCount + 1);
        }
    }

    private static void insertIndent(int indentCount) {
        while (--indentCount >= 0)
            System.out.print("  ");
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
