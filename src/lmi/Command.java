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
import static lmi.Constant.BoundingBox.*;

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

    // Wrapping ObjectFinder
    static Void objectInitWithRootWidget() {
        ObjectFinder.init();
        ObjectFinder.moveForward(ObjectShadow.rootWidget());
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

    // Debug
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
        System.out.println("resource name: " + Gob.resourceName(Self.gob()));
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

    static Void h() {
        Self.moveWestTile();
        return null;
    }

    static Void j() {
        Self.moveSouthTile();
        return null;
    }

    static Void jIterate() {
        while (true) {
            haven.Coord destination = Self.locationInCoord().add(0, 1);
            if (Self.move(destination) != SC_SUCCEEDED) break;
            haven.Coord currentLocation = Self.locationInCoord();
            System.out.println("current location: " + currentLocation);
        }
        return null;
    }

    static Void k() {
        Self.moveNorthTile();
        return null;
    }

    static Void l() {
        Self.moveEastTile();
        return null;
    }

    static Void describeAllGob() {
        int count = 0;
        java.util.Iterator<haven.Gob> iterator = Gob.iterator();
        while (iterator.hasNext()) {
            ++count;
            haven.Gob gob = iterator.next();
            System.out.println(Gob.resourceName(gob));
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

    static Void liftClosestGob() {
        haven.Gob closestGob = Gob.closestGob();
        final StatusCode result = Self.lift(closestGob);
        Util.debugPrint(Command.class, "result: " + result);
        return null;
    }

    static Void putNorthTile() {
        haven.Coord2d location = Coordinate.northTile(Self.location());
        haven.Coord locationInCoord = Coordinate.toCoord(location);
        WidgetMessageHandler.put(locationInCoord);
        return null;
    }

    static Void describeClosestGob() {
        haven.Gob closestGob = Gob.closestGob();
        System.out.println("[closest gob] " + Gob.resourceName(closestGob));
        System.out.println("[disstance] " + Self.distance(closestGob));
        return null;
    }

    static Void describeClosestGobOverlay() {
        haven.Gob closestGob = Gob.closestGob();
        System.out.println("[closest gob] " + Gob.resourceName(closestGob));
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
        haven.Gob closestGob = Gob.closestGob();
        java.util.Map<Class<? extends haven.GAttrib>, haven.GAttrib> attributeMap = haven.LMI.gobAttr(closestGob);
        String resourceName = Gob.resourceName(closestGob);
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
        haven.Coord2d destination = Coordinate.newCoordinateByOffset(Self.location(), 33.0, 33.0);
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
        haven.Gob closestGob = Gob.closestGob();
        final haven.Resource resource = Gob.resource(closestGob);
        if(resource == null) {
            System.out.println("[resource is null]");
            return null;
        }
        haven.ResDrawable resourceDrawable = (haven.ResDrawable)Gob.attribute(closestGob, haven.ResDrawable.class);
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

    static haven.Gob storedGob_ = null;
    static Void storeClosestGob() {
        storedGob_ = Gob.closestGob();
        return null;
    }

    static Void describeStoredGob() {
        Util.debugPrint(Command.class, "resource name: " + Gob.resourceName(storedGob_));
        Util.debugPrint(Command.class, "removed: " + storedGob_.removed);
        Util.debugPrint(Command.class, "location: " + Gob.location(storedGob_));
        Util.debugPrint(Command.class, "location in coord: " + Gob.locationInCoord(storedGob_));
        return null;
    }

    static Void describeSelfPose() {
        Array<String> poseArray = Gob.poseArray(Self.gob());
        if (poseArray == null)
            Util.debugPrint(Command.class, "no pose");
        for (String pose : poseArray)
            System.out.println("[pose name] " + pose);
        return null;
    }

    static Void gatherClosestGob() {
        String action = lmi.Scanner.nextLineWithPrompt("enter action");
        haven.Gob closestGob = Gob.closestGob();
        final Constant.StatusCode result = FlowerMenuHandler.choose(closestGob, MI_DEFAULT, action);
        Util.debugPrint(Command.class, "FlowerMenuHandler.choose() result " + result);
        Self.moveNorthTile();
        return null;
    }

    static Void test2() {
        System.out.println("click gob to inspect!");
        haven.Gob gob = ClickManager.getGob();
        Util.debugPrint(Command.class, "resource name: " + Gob.resourceName(gob));

        System.out.println("click gob to inspect!");
        gob = ClickManager.getGob();
        Util.debugPrint(Command.class, "resource name: " + Gob.resourceName(gob));
        return null;
    }

    static Void test() {
        haven.Coord northTile = Coordinate.northTile(Self.locationInCoord());

        Self.lift(storedGob_);
        Self.put(northTile);
        return null;
    }

    static Void investigateGobBoundingBoxWidth() {
        System.out.println("click gob of standard!");
        haven.Gob standardGob = ClickManager.getGob();

        System.out.println("click next gob to move!");
        haven.Gob variantGob = ClickManager.getGob();

        haven.Coord standardPoint = Coordinate.center(Self.locationInCoord());
        Self.lift(standardGob);
        Self.move(standardPoint.add(0, 2048));
        Self.put(standardPoint);

        final int start = 1024 / 8 * 3 + 1;
        int variant = start;
        while (true) {
            final haven.Coord variantPoint = standardPoint.add(variant, 0);
            if (carryWidth_(variantGob, variantPoint) != SC_SUCCEEDED) break;
            System.out.println("succeeded coord: " + variantPoint);
            --variant;
        }

        System.out.println("failed variant is " + variant);

        return null;
    }

    private static StatusCode carryWidth_(haven.Gob gob, haven.Coord putPoint) {
        Self.lift(gob);
        Self.move(putPoint.add(0, 2048));
        return Self.put(putPoint);
    }

    static Void investigateGobBoundingBoxHeight() {
        System.out.println("click gob of standard!");
        haven.Gob standardGob = ClickManager.getGob();

        System.out.println("click next gob to move!");
        haven.Gob variantGob = ClickManager.getGob();

        haven.Coord standardPoint = Coordinate.center(Self.locationInCoord());
        Self.lift(standardGob);
        Self.move(standardPoint.add(0, 2048));
        Self.put(standardPoint);

        final int start = 1024 / 8 * 13;
        int variant = start;
        while (true) {
            final haven.Coord variantPoint = standardPoint.add(0, variant);
            if (carryHeight_(variantGob, variantPoint) != SC_SUCCEEDED) break;
            System.out.println("succeeded coord: " + variantPoint);
            --variant;
        }

        System.out.println("failed variant is " + variant);

        return null;
    }

    private static StatusCode carryHeight_(haven.Gob gob, haven.Coord putPoint) {
        Self.lift(gob);
        return Self.put(putPoint);
    }

    static Void investigateSelfBoundingBoxWidthOnce() {
        System.out.println("click gob of standard!");
        haven.Gob standardGob = ClickManager.getGob();

        String variantString = lmi.Scanner.nextLineWithPrompt("enter variant");
        final int variant = Util.stoi(variantString);


        if (checkSelfVariantWidth(standardGob, variant) == SC_SUCCEEDED)
            System.out.println("succeeded");
        else
            System.out.println("failed");

        return null;
    }

    static Void investigateSelfBoundingBoxWidth() {
        System.out.println("click gob of standard!");
        haven.Gob standardGob = ClickManager.getGob();

        final int start = 512;
        int variant = start;
        while (true) {
            if (checkSelfVariantWidth(standardGob, variant) != SC_SUCCEEDED) break;
            variant -= 1;
        }

        System.out.println("failed variant is " + variant);

        return null;
    }

    private static StatusCode checkSelfVariantWidth(haven.Gob standardGob, int variant) {
        final haven.Coord variantPoint = Gob.locationInCoord(standardGob).add(variant, 0);
        haven.Coord firstStep = Coordinate.northTile(variantPoint);

        Self.move(firstStep);
        final StatusCode result = Self.move(variantPoint);
        if (result != SC_SUCCEEDED) return result;

        final double distance = Gob.distance(standardGob, Self.gob());
        System.out.println("succeeded coord: " + variantPoint + ", distance: " + distance);

        return SC_SUCCEEDED;
    }

    static Void test3() {
        final haven.Coord locationInCoord = Self.locationInCoord();
        final haven.Coord centerPosition = Coordinate.center(locationInCoord);

        System.out.println("첫 번째 로그를 선택해주세요");
        haven.Gob firstLog = ClickManager.getGob();

        System.out.println("두 번째 로그를 선택해주세요");
        haven.Gob secondLog = ClickManager.getGob();

        Self.lift(firstLog);
        Self.move(centerPosition.add(- BW_LOG / 2 - BW_BODY / 2, (BH_LOG + BH_BODY) / 2));
        Self.put(centerPosition.add(- BW_LOG / 2 - BW_BODY / 2, 0));

        Self.lift(secondLog);
        Self.move(centerPosition.add(BW_LOG / 2 + BW_BODY / 2, (BH_LOG + BH_BODY) / 2));
        Self.put(centerPosition.add(BW_LOG / 2 + BW_BODY / 2, 0));

        return null;
    }
}
