package lmi;

import java.util.Set;
import java.util.TreeMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import lmi.api.*;

class Command {
    // type define
    private static class CommandMap extends TreeMap<String, Method> {};

    // fields
    private static CommandMap map_;

    // macro command
    static Void macroInterrupt() {
        MacroThread.interrupt();
        return null;
    }

    static Void macroConnect() {
        MacroThread.start(new lmi.macro.Connect());
        return null;
    }

    static Void macroPatrol000() {
        MacroThread.start(new lmi.macro.Patrol000());
        return null;
    }

    static Void macroPatrol001() {
        MacroThread.start(new lmi.macro.Patrol001());
        return null;
    }

    static Void macroPatrol002() {
        MacroThread.start(new lmi.macro.Patrol002());
        return null;
    }

    static Void macroTest() {
        MacroThread.start(new lmi.macro.Test());
        return null;
    }

    static Void macroDescribeClickedGob() {
        lmi.api.Util.describeClickedGob();
        return null;
    }

    // action command
    static Void dig() {
        lmi.api.Self.dig();
        return null;
    }

    // etc command
    static Void printObjectShadow() {
        Debug.describeField(System.out, lmi.ObjectShadow.class);
        return null;
    }

    static Void recordMouseLocation() {
        lmi.api.AWTEventGenerator.setMouseLocation(ObjectShadow.ui().mc.x, ObjectShadow.ui().mc.y);
        return null;
    }

    static Void printRecordedMouseLocation() {
        lmi.api.AWTEventGenerator.printRecordedMouseLocation();
        return null;
    }

//      // awt commands
//      static Void awtGenerateMouseClick() {
//          lmi.api.AWTEventGenerator.generateMouseClickGeneral(java.awt.event.MouseEvent.BUTTON3);
//          return null;
//      }
//  
//      static Void awtGenerateMouseRightClick() {
//          lmi.api.AWTEventGenerator.generateMouseClickModified(0, java.awt.event.MouseEvent.BUTTON3);
//          return null;
//      }
//  
//      static Void toggleEquipment() {
//          lmi.api.AWTEventGenerator.generateCtrlE();
//          return null;
//      }
//  
//      static Void openAxeCraftWindow() {
//          lmi.api.AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_C);
//          lmi.api.AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_C);
//          lmi.api.AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_T);
//          lmi.api.AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_A);
//          lmi.api.AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_T);
//          return null;
//      }
//  
//      static Void typeEnter() {
//          lmi.api.AWTEventGenerator.generateKeyPushUpSpecialKey(java.awt.event.KeyEvent.VK_ENTER);
//          return null;
//      }
//  
//      static Void typeTab() {
//          lmi.api.AWTEventGenerator.generateKeyPushUpSpecialKey(java.awt.event.KeyEvent.VK_TAB);
//          return null;
//      }

    // Wrapping ObjectFinder
    static Void objectInit() {
        wrapObjectFinderFind(Util.MemberType.FIELD, ObjectShadow.class, true);
        return null;
    }

    static Void objectInitByClickedGob() {
        ObjectFinder.init();
        ObjectFinder.moveForward(lmi.api.Util.clickedGob());
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
        Debug.describeField(ObjectFinder.last());
        return null;
    }

    static Void objectChangeToReturnValueOfMethod() {
        wrapObjectFinderFind(Util.MemberType.METHOD, null, true);
        return null;
    }

    static Void objectDescribe() {
        Debug.describeField(ObjectFinder.last());
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
            Debug.describeField(element);
        }
        return null;
    }

    private static void wrapObjectFinderFind(Util.MemberType type, Class classObjectToReset, boolean willAppend) {
        Debug.describeClassNameHashCodeWithTag("current: ", ObjectFinder.last());

        Object object = null;
        try {
            object = ObjectFinder.find(type, classObjectToReset);
            Debug.describeField(object);
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

    // etc
    static Void toggleDebugIsPrint() {
        Debug.toggleIsPrint();
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

    // test command
    static Void test() {
        test008();
        return null;
    }

    private static void test000() {
        final haven.Gob gob = lmi.api.Util.clickedGob();
        lmi.api.FlowerMenuHandler.chooseByGobAndPetalName(gob, Constant.Interaction.TAKE_BRANCH);
    }

    private static void test001() {
        System.out.println(lmi.api.Self.hardHitPoint());
        System.out.println(lmi.api.Self.softHitPoint());
        System.out.println(lmi.api.Self.stamina());
        System.out.println(lmi.api.Self.energy());
    }

    private static void test002() {
        java.util.Map<Class<? extends haven.GAttrib>, haven.GAttrib> map = haven.LMI.gobAttr(lmi.api.Self.gob());
        map.forEach((unused, value) -> {
                Debug.describeField(value);
                });
    }

    private static void test003() {
        haven.Coord2d targetLocation = lmi.api.CoordinateHandler.northTile(lmi.api.Self.location());
        lmi.api.Self.move(targetLocation);
        while (!lmi.api.Self.isArrived(targetLocation)) {
            try {
                Thread.sleep(lmi.Constant.Time.GENERAL_SLEEP);
            } catch (Exception e) { e.printStackTrace(); }
            Debug.describeField(lmi.api.Self.location());
            Debug.describeField(targetLocation);
        }
        System.out.println("[self is arrived]");
    }

    private static void test004() {
        haven.Coord2d targetLocation = lmi.api.CoordinateHandler.newCoordinateByOffset(lmi.api.Self.location(), 33.0, 33.0);
        try {
            if (lmi.api.Self.moveAndWaitArriving(targetLocation))
                System.out.println("[moveAndWaitArriving() success]");
            else
                System.out.println("[moveAndWaitArriving() failed]");
        } catch (Exception e) { System.out.println("[interrupted]"); }
    }

    private static void test005() {
        int count = 0;
        java.util.Iterator<haven.Gob> iterator = GobHandler.iterator();
        while (iterator.hasNext()) {
            ++count;
            haven.Gob gob = iterator.next();
            System.out.println(GobHandler.resourceName(gob));
            System.out.println(count);
        }
    }

    private static void test006() {
        haven.Gob gob = GobHandler.closestGob();
        System.out.println("[distance of closest gob] " + Self.distance(gob));
        System.out.println("[resource name of closest gob] " + GobHandler.resourceName(gob));
//          Debug.describeField(Self.gob());
//          Debug.describeField(gob);
    }

    private static void test007() {
        try {
            Self.moveCenter();
        } catch (Exception e) { System.out.println("[test007() is interrupted]"); }
    }

    private static void test008() {
        try {
            Self.moveCenter();
            for (int count = 0; count < 10; ++count)
                Self.moveNorthTile();
        } catch (Exception e) { System.out.println("[test008() is interrupted]"); }
    }
}
