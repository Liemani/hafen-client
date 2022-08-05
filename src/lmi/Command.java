package lmi;

import java.util.Set;
import java.util.TreeMap;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.lang.reflect.Field;

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
        Debug.debugDescribeField(System.out, lmi.ObjectShadow.class);

        return null;
    }

    static Void printListOfMainFrame() {
        lmi.ObjectShadow.mainFrame_.list(System.out, 4);

        return null;
    }

    static Void recordMouseLocation() {
        AWTEventGenerator.setMouseLocation(ObjectShadow.ui_.mc.x, ObjectShadow.ui_.mc.y);

        return null;
    }

    static Void printRecordedMouseLocation() {
        Debug.debugDescribeField(AWTEventGenerator.class);

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

    static haven.Widget currentWidget_ = null;
    static Void resetCurrentWidgetToRoot() {
        currentWidget_ = ObjectShadow.rootWidget_;

        return null;
    }

//      static Void moveNextWidget() {
//          if (currentWidget_.next != null) {
//              currentWidget_ = currentWidget_.next;
//              printCurrentWidgetClassName();
//          }
//          else
//              System.out.println("next widget is null");
//  
//          return null;
//      }

//      static Void movePreviousWidget() {
//          if (currentWidget_.prev != null) {
//              currentWidget_ = currentWidget_.prev;
//              printCurrentWidgetClassName();
//          }
//          else
//              System.out.println("previous widget is null");
//  
//          return null;
//      }

//      static Void moveChildWidget() {
//          if (currentWidget_.child != null) {
//              currentWidget_ = currentWidget_.child;
//              printCurrentWidgetClassName();
//          }
//          else
//              System.out.println("child widget is null");
//  
//          return null;
//      }

//      static Void moveParentWidget() {
//          if (currentWidget_.parent != null) {
//              currentWidget_ = currentWidget_.parent;
//              printCurrentWidgetClassName();
//          }
//          else
//              System.out.println("parent widget is null");
//  
//          return null;
//      }

    static Void iterateWidget() {
        iterateWidget(currentWidget_, 0);

        return null;
    }

    static Object object_ = null;
    static Void objectInit() {
        controlObject(true, true);
        return null;
    }

    static Void objectPeek() {
        controlObject(false, false);
        return null;
    }

    static Void objectChange() {
        controlObject(false, true);
        return null;
    }

    static Void objectPrint() {
        Debug.debugDescribeField(object_);
        return null;
    }

    // private commands
    private static void controlObject(boolean willReset, boolean willSet) {
        Debug.debugDescribeClassNameHashCodeWithTag("object_: ", object_);
        if (!willReset && object_ == null) {
            System.out.println("cannot progress more!");
            return;
        }

        Class classObject = willReset ? ObjectShadow.class : object_.getClass();
        Object object = null;
        try {
            object = getObjectByInput(classObject);
            Debug.debugDescribeField(object);
        } catch (Exception e) {
            System.out.println(e.getMessage() + ": unknown field name");
            e.printStackTrace();
        }
        if (willSet && object != null)
            object_ = object;
    }

    private static Object getObjectByInput(Class classObject) throws Exception {
        Object object = null;

        printFieldList(classObject);
        String fieldName = lmi.Scanner.nextLineWithPrompt("enter field name");
        object = Util.getFieldValueFromObjectByNameAsClass(object_, fieldName, classObject);

        return object;
    }

    private static void printFieldList(Class classObject) {
        System.out.println("field list:");
        for (; classObject != Object.class; classObject = classObject.getSuperclass()) {
            Field[] fields = classObject.getDeclaredFields();
            for (Field field : fields) {
                if (Util.fieldHasModifier(field, Modifier.PUBLIC)) {
                    System.out.print("  ");
                    System.out.println(field.getName());
                }
            }
        }
    }

    private static void iterateWidget(haven.Widget widget, int indentCount) {
        haven.Widget child = widget.child;

        for (; child != null; child = child.next) {
            Util.insertIndent(indentCount);
            System.out.println(child.getClass().getName());
            iterateWidget(child, indentCount + 1);
        }
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
