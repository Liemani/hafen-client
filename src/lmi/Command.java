package lmi;

import java.util.Set;
import java.util.TreeMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import lmi.api.*;
import lmi.collection.Array;

// constant
import lmi.Constant.StatusCode;
import static lmi.Constant.StatusCode.*;
import static lmi.Constant.MeshId.*;
import static lmi.Constant.gfx.borka.*;

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
    static Void describeSelf() {
        System.out.println("resource name: " + GobHandler.resourceName(Self.gob()));
        System.out.println("Self.location(): " + Self.location());
        System.out.println("Self.locationInCoord(): " + Self.locationInCoord());
        System.out.println("Self.hardHitPoint(): " + Self.hardHitPoint());
        System.out.println("Self.softHitPoint(): " + Self.softHitPoint());
        System.out.println("Self.stamina(): " + Self.stamina());
        System.out.println("Self.energy(): " + Self.energy());
        return null;
    }

    static Void describeSelfAttribute() {
        java.util.Map<Class<? extends haven.GAttrib>, haven.GAttrib> map = haven.LMI.gobAttr(Self.gob());
        map.forEach((unused, value) -> {
                Debug.describeField(value);
                });
        return null;
    }

    static Void moveNorthTile() {
        Self.moveNorthTile();
        return null;
    }

    static Void moveEastSouth() {
        haven.Coord2d targetLocation = CoordinateHandler.newCoordinateByOffset(Self.location(), 33.0, 33.0);
        final Constant.StatusCode result = Self.move(targetLocation);
        Util.debugPrint(Command.class, "result: " + result);
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
        final StatusCode result = Self.moveCenter();
        lmi.Util.debugPrint(Command.class, "result: " + result);
        return null;
    }

    static Void describeCursorGItem() {
        haven.Widget gItem = WidgetManager.cursorGItem();
        Debug.describeField(gItem);
        return null;
    }

//      static Void liftClosestGob() {
//          haven.Gob closestGob = GobHandler.closestGob();
//          WidgetMessageHandler.lift(closestGob);
//          return null;
//      }

    static Void putNorthTile() {
        haven.Coord2d location = CoordinateHandler.northTile(Self.location());
        haven.Coord locationInCoord = CoordinateHandler.convertCoord2dToCoord(location);
        WidgetMessageHandler.put(locationInCoord);
        return null;
    }

    static Void describeClosestGob() {
        haven.Gob closestGob = GobHandler.closestGob();
        System.out.println("[closest gob] " + GobHandler.resourceName(closestGob));
        System.out.println("[disstance] " + Self.distance(closestGob));
        return null;
    }

    static Void describeClosestGobOverlay() {
        haven.Gob closestGob = GobHandler.closestGob();
        System.out.println("[closest gob] " + GobHandler.resourceName(closestGob));
        for (haven.Gob.Overlay overlay : closestGob.ols) {
            try {
                System.out.println(overlay.res.get().name);
                for (byte b : overlay.sdt.rbuf)
                    System.out.print(" " + b);
            } catch (NullPointerException e) {
                System.out.println("[describeClosestGobOverlay() null pointer exception has occured]");
            }
        }
        return null;
    }

    static Void describeClosestGobAttribute() {
        haven.Gob closestGob = GobHandler.closestGob();
        java.util.Map<Class<? extends haven.GAttrib>, haven.GAttrib> attributeMap = haven.LMI.gobAttr(closestGob);
        String resourceName = GobHandler.resourceName(closestGob);
        System.out.println("[resource name] " + resourceName);
        for (haven.GAttrib attribute : attributeMap.values()) {
            if (attribute instanceof haven.GobIcon
                    || attribute instanceof haven.Drawable
                    || attribute instanceof haven.KinInfo
                    || attribute instanceof haven.GobHealth) {
                if (attribute instanceof haven.ResDrawable) {
                    Debug.describeField(attribute);
                } else {
                    Debug.describeClassNameHashCodeWithTag("[attribute] ", attribute);
                }
            } else {
                Debug.describeField(attribute);
            }
        }
        return null;
    }

    static Void move() {
        haven.Coord2d destination = CoordinateHandler.newCoordinateByOffset(Self.location(), 33.0, 33.0);
        final Constant.StatusCode result = Self.move(destination);
        Util.debugPrint(Command.class, "result: " + result);
        return null;
    }

    static Void moveNorthTileTenTimes() {
        {
            final StatusCode result = Self.moveCenter();
            Util.debugPrint(Command.class, "result: " + result);
        }
        for (int count = 0; count < 10; ++count) {
            final StatusCode result = Self.moveNorthTile();
            Util.debugPrint(Command.class, "result: " + result);
        }
        return null;
    }

    static Void describeClosestGobSdt() {
        haven.Gob closestGob = GobHandler.closestGob();
        final haven.Resource resource = GobHandler.resource(closestGob);
        if(resource == null) {
            System.out.println("[resource is null]");
            return null;
        }
        haven.ResDrawable resourceDrawable = (haven.ResDrawable)GobHandler.attribute(closestGob, haven.ResDrawable.class);
        byte[] buffer = haven.LMI.resourceDrawableBuffer(resourceDrawable);
        if (buffer == null) {
            System.out.println("[buffer is null]");
            return null;
        }
        System.out.print("[buffer] length: " + buffer.length);
        for (byte b : buffer)
            System.out.print(" " + b);
        System.out.println();
        return null;
    }

//      static Void investigateGobBoundingBoxWidth() {
//          haven.Gob closestGob = GobHandler.closestGob();
//          double start = 1024;
//          return null;
//      }

    static haven.Gob storedGob_ = null;
    static Void storeClosestGob() {
        storedGob_ = GobHandler.closestGob();
        return null;
    }

    static Void describeStoredGob() {
        Util.debugPrint(Command.class, "resource name: " + GobHandler.resourceName(storedGob_));
        Util.debugPrint(Command.class, "removed: " + storedGob_.removed);
        Util.debugPrint(Command.class, "location(storedGob_): " + GobHandler.location(storedGob_));
        return null;
    }

    static Void describeSelfPose() {
        Array<String> poseArray = GobHandler.poseArray(Self.gob());
        if (poseArray == null)
            Util.debugPrint(Command.class, "no pose");
        for (String pose : poseArray)
            System.out.println("[pose name] " + pose);
        return null;
    }

    static Void gatherClosestGob() {
        String action = lmi.Scanner.nextLineWithPrompt("enter action");
        haven.Gob closestGob = GobHandler.closestGob();
        final Constant.StatusCode result = FlowerMenuHandler.choose(closestGob, MI_DEFAULT, action);
        Util.debugPrint(Command.class, "FlowerMenuHandler.choose() result " + result);
        Self.moveNorthTile();
        return null;
    }

//      static Void test() {
//          Array<String> array = new Array<String>();
//  
//          array.append("hi");
//          array.append("there");
//          array.append("nice");
//          array.append("to");
//          array.append("meet");
//          array.append("you");
//  
//          if (array.contains("hello"))
//              System.out.println("\"hello\" is contains in array");
//          else
//              System.out.println("\"hello\" is not contains in array");
//  
//          if (array.contains("meet"))
//              System.out.println("\"meet\" is contains in array");
//          else
//              System.out.println("\"meet\" is not contains in array");
//  
//          return null;
//      }

    static Void test() {
        if (GobHandler.hasPose(Self.gob(), RN_SAWING))
            System.out.println("Self.gob has pose RN_SAWING");
        else
            System.out.println("Self.gob has not pose RN_SAWING");
        if (GobHandler.hasPose(Self.gob(), RN_THINKAN))
            System.out.println("Self.gob has pose RN_THINKAN");
        else
            System.out.println("Self.gob has not pose RN_THINKAN");
        return null;
    }
}
