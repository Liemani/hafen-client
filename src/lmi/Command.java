package lmi;

import java.util.Set;
import java.util.TreeMap;

import java.lang.reflect.Field;
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

    static Void dig() {
        Player.dig();
        return null;
    }

    static Void printGob() {
        Debug.debugDescribeField(Util.gob);
        return null;
    }

//      static Void mapClickMove() {
//          if (gob == null)
//              return null;
//  
//          mapClick(Util.gob.rc.x, Util.gob.rc.y, 1, 0);
//          return null;
//      }

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

    // Wrapping ObjectFinder
    static Void objectInit() {
        wrapObjectFinderFind(Util.MemberType.FIELD, ObjectShadow.class, true);
        return null;
    }

    static Void objectPeek() {
        wrapObjectFinderFind(Util.MemberType.FIELD, null, false);
        return null;
    }

    static Void objectChange() {
        wrapObjectFinderFind(Util.MemberType.FIELD, null, true);
        return null;
    }

    static Void objectChangePrevious() {
        if (ObjectFinder.isEmpty()) {
            System.out.println("there is no previous object");
            return null;
        }

        ObjectFinder.removeLast();
        Debug.debugDescribeField(ObjectFinder.last());
        return null;
    }

    static Void objectPrintResultOfInvokedMethod() {
        wrapObjectFinderFind(Util.MemberType.METHOD, null, false);
        return null;
    }

    static Void objectSetResultOfInvokedMethod() {
        wrapObjectFinderFind(Util.MemberType.METHOD, null, true);
        return null;
    }

    static Void objectPrint() {
        Debug.debugDescribeField(ObjectFinder.last());
        return null;
    }

    private static void wrapObjectFinderFind(Util.MemberType type, Class classObjectToReset, boolean willAppend) {
        Debug.debugDescribeClassNameHashCodeWithTag("current: ", ObjectFinder.last());

        Object object = null;
        try {
            object = ObjectFinder.find(type, classObjectToReset);
            Debug.debugDescribeField(object);
            if (willAppend)
                ObjectFinder.append(object);
        } catch (Exception e) { e.printStackTrace(); }
    }

    // iterateWidget;
    private static void iterateWidget(haven.Widget widget, int indentCount) {
        haven.Widget child = widget.child;

        for (; child != null; child = child.next) {
            Util.insertIndent(indentCount);
            System.out.println(child.getClass().getName());
            iterateWidget(child, indentCount + 1);
        }
    }

    // package method
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

    private static void mapClick(double x, double y, int btn, int mod) {
        ObjectShadow.mapView_.wdgmsg("click", getCenterScreenCoord(), new haven.Coord2d(x, y).floor(haven.OCache.posres), btn, mod);
    }

    private static haven.Coord getCenterScreenCoord() {
		return ObjectShadow.mapView_.sz.div(2);
	}
}
