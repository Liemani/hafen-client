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

    // commands
    static Void commandTemplate() {
        // code here...
        return null;
    }

    static Void autoConnect() {
        macroThread_ = new Thread(new lmi.macro.AutoConnect());
        macroThread_.start();
        return null;
    }

    static Void selectCharacterTest() {
        lmi.macro.Util.selectCharacter("test");
        return null;
    }

    static Void dig() {
        lmi.macro.Player.dig();
        return null;
    }

    static Void playerDescribe() {
        Player.debugDescribe();
        return null;
    }

    static Void printLastClickedGob() {
        lmi.macro.Util.printLastClickedGob();
        return null;
    }

    static Void mapClickEast50() {
        haven.Coord2d playerLocation = lmi.macro.Player.location();
        lmi.macro.Player.mapClickLeftMouseButton(playerLocation.add(50.0, 0.0), 0);
        return null;
    }

    static Void mapClickSouth1() {
        haven.Coord playerLocationInCoord = Player.location().floor(haven.OCache.posres);
        haven.Coord playerLocationInCoordPlus1 = playerLocationInCoord.add(1, 0);
        lmi.macro.Player.mapClickInCoord(playerLocationInCoordPlus1, 1, 0);
        return null;
    }

    // control macro
    static Void runPatrol000() {
        macroThread_ = new Thread(new lmi.macro.Patrol000());
        macroThread_.start();
        return null;
    }

    static Void interruptPatrol000() {
        macroThread_.interrupt();
        return null;
    }

    static Void runPatrol001() {
        macroThread_ = new Thread(new lmi.macro.Patrol000());
        macroThread_.start();
        return null;
    }

    static Void interruptPatrol001() {
        macroThread_.interrupt();
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
//  
//          return null;
//      }
//  
//      static Void awtGenerateMouseRightClick() {
//          lmi.macro.AWTEventGenerator.generateMouseClickModified(0, java.awt.event.MouseEvent.BUTTON3);
//  
//          return null;
//      }
//  
//      static Void toggleEquipment() {
//          lmi.macro.AWTEventGenerator.generateCtrlE();
//  
//          return null;
//      }
//  
//      static Void openAxeCraftWindow() {
//          lmi.macro.AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_C);
//          lmi.macro.AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_C);
//          lmi.macro.AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_T);
//          lmi.macro.AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_A);
//          lmi.macro.AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_T);
//  
//          return null;
//      }
//  
//      static Void typeEnter() {
//          lmi.macro.AWTEventGenerator.generateKeyPushUpSpecialKey(java.awt.event.KeyEvent.VK_ENTER);
//  
//          return null;
//      }
//  
//      static Void typeTab() {
//          lmi.macro.AWTEventGenerator.generateKeyPushUpSpecialKey(java.awt.event.KeyEvent.VK_TAB);
//  
//          return null;
//      }

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

    static Void objectChangeUndo() {
        if (ObjectFinder.isEmpty()) {
            System.out.println("there is no previous object");
            return null;
        }

        ObjectFinder.moveBackward();
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

    static Void objectPrintAsIterable() {
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
            if (willAppend)
                ObjectFinder.moveForward(object);
        } catch (Exception e) { e.printStackTrace(); }
    }

    static Void listCurrentObject() {
        ObjectFinder.listLast();
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
