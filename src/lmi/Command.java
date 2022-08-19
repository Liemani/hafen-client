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

    // etc command
    static Void recordMouseLocation() {
        AWTEventGenerator.setMouseLocation(ObjectShadow.ui().mc.x, ObjectShadow.ui().mc.y);
        return null;
    }

    static Void printRecordedMouseLocation() {
        AWTEventGenerator.printRecordedMouseLocation();
        return null;
    }

//      // awt commands
//      static Void awtGenerateMouseClick() {
//          AWTEventGenerator.generateMouseClickGeneral(java.awt.event.MouseEvent.BUTTON3);
//          return null;
//      }
//  
//      static Void awtGenerateMouseRightClick() {
//          AWTEventGenerator.generateMouseClickModified(0, java.awt.event.MouseEvent.BUTTON3);
//          return null;
//      }
//  
//      static Void toggleEquipment() {
//          AWTEventGenerator.generateCtrlE();
//          return null;
//      }
//  
//      static Void openAxeCraftWindow() {
//          AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_C);
//          AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_C);
//          AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_T);
//          AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_A);
//          AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_T);
//          return null;
//      }
//  
//      static Void typeEnter() {
//          AWTEventGenerator.generateKeyPushUpSpecialKey(java.awt.event.KeyEvent.VK_ENTER);
//          return null;
//      }
//  
//      static Void typeTab() {
//          AWTEventGenerator.generateKeyPushUpSpecialKey(java.awt.event.KeyEvent.VK_TAB);
//          return null;
//      }

    // Wrapping ObjectFinder
    static Void objectInitWithRootWidget() {
        ObjectFinder.init();
        ObjectFinder.moveForward(ObjectShadow.rootWidget());
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

    // TODO modified ObjectShadow's fields access modifier to private,
    //  and now can't use this features for objectInit()
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
            if (!Util.methodHasModifier(method, Modifier.PUBLIC)
                    && !Util.methodHasModifier(method, Modifier.PRIVATE)) {
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
    static Void interactWaitClosestGob() {
        final haven.Gob gob = GobHandler.closestGob();
        try {
            FlowerMenuHandler.interactWait(gob, Constant.MeshId.NONE);
//              FlowerMenuHandler.chooseByGobAndPetalName(gob, Constant.Interaction.TAKE_BRANCH);
        } catch (Exception e) {}
        return null;
    }

    static Void describeSelf() {
        System.out.println(Self.hardHitPoint());
        System.out.println(Self.softHitPoint());
        System.out.println(Self.stamina());
        System.out.println(Self.energy());
        return null;
    }

    static Void describeSelfAttribute() {
        java.util.Map<Class<? extends haven.GAttrib>, haven.GAttrib> map = haven.LMI.gobAttr(Self.gob());
        map.forEach((unused, value) -> {
                Debug.describeField(value);
                });
        return null;
    }

    static Void moveNorthTileWait() {
        haven.Coord2d targetLocation = CoordinateHandler.northTile(Self.location());
        Self.move(targetLocation);
        while (!Self.isArrived(targetLocation)) {
            try {
                Thread.sleep(lmi.Constant.Time.GENERAL_SLEEP);
            } catch (Exception e) { e.printStackTrace(); }
            Debug.describeField(Self.location());
            Debug.describeField(targetLocation);
        }
        System.out.println("[self is arrived]");
        return null;
    }

    static Void moveEastSouthWait() {
        haven.Coord2d targetLocation = CoordinateHandler.newCoordinateByOffset(Self.location(), 33.0, 33.0);
        try {
            if (Self.moveAndWaitArriving(targetLocation))
                System.out.println("[moveAndWaitArriving() success]");
            else
                System.out.println("[moveAndWaitArriving() failed]");
        } catch (Exception e) { System.out.println("[interrupted]"); }
        return null;
    }

    static Void describeAllGob() {
        int count = 0;
        java.util.Iterator<haven.Gob> iterator = GobHandler.iterator();
        while (iterator.hasNext()) {
            ++count;
            haven.Gob gob = iterator.next();
            System.out.println(GobHandler.resourceName(gob));
            System.out.println(count);
        }
        return null;
    }

    static Void moveCenter() {
        try {
            Self.moveCenter();
        } catch (Exception e) { System.out.println("[moveCenter() is interrupted]"); }
        return null;
    }

    static Void moveNorthTileTenTimes() {
        try {
            Self.moveCenter();
            for (int count = 0; count < 10; ++count)
                Self.moveNorthTile();
        } catch (Exception e) { System.out.println("[moveNorthTileTenTimes() is interrupted]"); }
        return null;
    }

    static Void describeCursorGItem() {
        haven.Widget gItem = WidgetManager.cursorGItem();
        Debug.describeField(gItem);
        return null;
    }

    static Void liftClosestGob() {
        haven.Gob gob = GobHandler.closestGob();
        WidgetMessageHandler.lift(gob);
        return null;
    }

    static Void putNorthTile() {
        haven.Coord2d location = CoordinateHandler.northTile(Self.location());
        haven.Coord locationInCoord = CoordinateHandler.convertCoord2dToCoord(location);
        WidgetMessageHandler.put(locationInCoord);
        return null;
    }

    static Void describeClosestGob() {
        haven.Gob gob = GobHandler.closestGob();
        System.out.println("[closest gob] " + GobHandler.resourceName(gob));
        System.out.println("[disstance] " + Self.distance(gob));
        return null;
    }

//      static Void
}
