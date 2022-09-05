package lmi;

// import haven package
import haven.Gob;
import haven.Coord;

import java.util.Set;
import java.util.TreeMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

// import lmi package
import lmi.api.*;
import lmi.collection.Array;
import lmi.graphic.Rect;

// constant
import lmi.Constant.StatusCode;
import static lmi.Constant.StatusCode.*;
import static lmi.Constant.MeshId.*;
import static lmi.Constant.gfx.borka.*;
import static lmi.Constant.BoundingBox.*;
import static lmi.Constant.*;

class Command {
    // type define
    private static class CommandMap extends TreeMap<String, Method> {};

    // fields
    private static CommandMap map_;

    // automation command
    static Void automationInterrupt() {
        AutomationThread.interrupt();
        return null;
    }

    static Void automationConnect() {
        AutomationThread.start(new lmi.automation.Connect());
        return null;
    }

    static Void automationPatrol000() {
        AutomationThread.start(new lmi.automation.Patrol000());
        return null;
    }

    static Void automationPatrol001() {
        AutomationThread.start(new lmi.automation.Patrol001());
        return null;
    }

    static Void automationPatrol002() {
        AutomationThread.start(new lmi.automation.Patrol002());
        return null;
    }

    static Void automationTest() {
        AutomationThread.start(new lmi.automation.Test());
        return null;
    }

    static Void automationCleanLog() {
        AutomationThread.start(new lmi.automation.CleanLog());
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
        System.out.println("resource name: " + Self.gob().resourceName());
        System.out.println("Self.location(): " + Self.location());
        System.out.println("Self.hardHitPoint(): " + Self.hardHitPoint());
        System.out.println("Self.softHitPoint(): " + Self.softHitPoint());
        System.out.println("Self.stamina(): " + Self.stamina());
        System.out.println("Self.energy(): " + Self.energy());
        return null;
    }

    static Void describeSelfAttribute() {
        java.util.Map<Class<? extends haven.GAttrib>, haven.GAttrib> map = Self.gob().attributeMap();
        map.forEach((unused, value) -> {
                Debug.describeField(value);
                });
        return null;
    }

    // Simple Move
    static Void h() {
        Self.moveWest();
        return null;
    }

    static Void j() {
        Self.moveSouth();
        return null;
    }

    static Void jIterate() {
        while (true) {
            Coord destination = Self.location().add(0, 1);
            if (Self.move(destination) != SC_SUCCEEDED) break;
            Coord currentLocation = Self.location();
            System.out.println("current location: " + currentLocation);
        }
        return null;
    }

    static Void k() {
        Self.moveNorth();
        return null;
    }

    static Void l() {
        Self.moveEast();
        return null;
    }

    static Void describeAllGob() {
        int count = 0;
        java.util.Iterator<Gob> iterator = lmi.api.Util.iterator();
        while (iterator.hasNext()) {
            ++count;
            Gob gob = iterator.next();
            System.out.println("[" + count + "] { virtual: " + gob.virtual + ", class: " + gob.getClass() + ", resource name: " + gob.resourceName() + " }");
        }
        return null;
    }

    static Void moveCenter() {
        final StatusCode result = Self.moveCenter();
        Util.debugPrint(Command.class, "result: " + result);
        return null;
    }

    static Void describeCursorGItem() {
        haven.Widget gItem = WidgetManager.cursorGItem();
        Debug.describeField(gItem);
        return null;
    }

    static Void describeClickedGob() {
        Gob gob = ClickManager.getGob();

        Util.debugPrint(Command.class, "resource name: " + gob.resourceName());
        Util.debugPrint(Command.class, "location: " + gob.location());
        Util.debugPrint(Command.class, "distance: " + Self.distance(gob));
        Util.debugPrint(Command.class, "removed: " + gob.removed);

        return null;
    }

    static Void describeClosestGobOverlay() {
        Gob closestGob = lmi.api.Util.closestGob();
        System.out.println("[closest gob] " + closestGob.resourceName());
        for (Gob.Overlay overlay : closestGob.ols) {
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
        Gob closestGob = lmi.api.Util.closestGob();
        java.util.Map<Class<? extends haven.GAttrib>, haven.GAttrib> attributeMap = closestGob.attributeMap();
        String resourceName = closestGob.resourceName();
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
        final Coord destination = Self.location().offset(TILE_IN_COORD * 3, TILE_IN_COORD * 3);
        final Constant.StatusCode result = Self.move(destination);
        Util.debugPrint(Command.class, "result: " + result);
        return null;
    }

    static Void moveNorthTenTimes() {
        {
            final StatusCode result = Self.moveCenter();
            Util.debugPrint(Command.class, "result: " + result);
        }
        for (int count = 0; count < 10; ++count) {
            final StatusCode result = Self.moveNorth();
            Util.debugPrint(Command.class, "result: " + result);
        }
        return null;
    }

    static Void describeClosestGobSdt() {
        Gob closestGob = lmi.api.Util.closestGob();
        final haven.Resource resource = closestGob.resource();
        if(resource == null) {
            System.out.println("[resource is null]");
            return null;
        }
        haven.ResDrawable resourceDrawable = (haven.ResDrawable)closestGob.attribute(haven.ResDrawable.class);
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

    static Void describeSelfPose() {
        Array<String> poseArray = Self.gob().poseArray();
        if (poseArray == null)
            Util.debugPrint(Command.class, "no pose");
        for (String pose : poseArray)
            System.out.println("[pose name] " + pose);
        return null;
    }

    static Void gatherClosestGob() {
        String action = lmi.Scanner.nextLineWithPrompt("enter action");
        Gob closestGob = lmi.api.Util.closestGob();
        final Constant.StatusCode result = FlowerMenuHandler.choose(closestGob, MI_DEFAULT, action);
        Util.debugPrint(Command.class, "FlowerMenuHandler.choose() result " + result);
        Self.moveNorth();
        return null;
    }

    static Void investigateGobBoundingBoxWidth() {
        System.out.println("click gob of standard!");
        Gob standardGob = ClickManager.getGob();

        System.out.println("click next gob to move!");
        Gob variantGob = ClickManager.getGob();

        Coord standardPoint = Self.location();
        Self.lift(standardGob);
        Self.move(standardPoint.add(0, 2048));
        Self.put(standardPoint);

        final int start = TILE_IN_COORD / 8 * 3 + 1;
        int variant = start;
        while (true) {
            final Coord variantPoint = standardPoint.add(variant, 0);
            if (carryWidth_(variantGob, variantPoint) != SC_SUCCEEDED) break;
            System.out.println("succeeded coord: " + variantPoint);
            --variant;
        }

        System.out.println("failed variant is " + variant);

        return null;
    }

    private static StatusCode carryWidth_(Gob gob, Coord putPoint) {
        Self.lift(gob);
        Self.move(putPoint.add(0, TILE_IN_COORD * 2));
        return Self.put(putPoint);
    }

    static Void investigateGobBoundingBoxHeight() {
        System.out.println("click gob of standard!");
        Gob standardGob = ClickManager.getGob();

        System.out.println("click next gob to move!");
        Gob variantGob = ClickManager.getGob();

        Coord standardPoint = Self.location().tileCenter();
        Self.lift(standardGob);
        Self.move(standardPoint.add(0, 2048));
        Self.put(standardPoint);

        final int start = 1024 / 8 * 13;
        int variant = start;
        while (true) {
            final Coord variantPoint = standardPoint.add(0, variant);
            if (carryHeight_(variantGob, variantPoint) != SC_SUCCEEDED) break;
            System.out.println("succeeded coord: " + variantPoint);
            --variant;
        }

        System.out.println("failed variant is " + variant);

        return null;
    }

    private static StatusCode carryHeight_(Gob gob, Coord putPoint) {
        Self.lift(gob);
        return Self.put(putPoint);
    }

    static Void investigateSelfBoundingBoxWidthOnce() {
        System.out.println("click gob of standard!");
        Gob standardGob = ClickManager.getGob();

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
        Gob standardGob = ClickManager.getGob();

        final int start = 512;
        int variant = start;
        while (true) {
            if (checkSelfVariantWidth(standardGob, variant) != SC_SUCCEEDED) break;
            variant -= 1;
        }

        System.out.println("failed variant is " + variant);

        return null;
    }

    private static StatusCode checkSelfVariantWidth(Gob standardGob, int variant) {
        final Coord variantPoint = standardGob.location().add(variant, 0);
        Coord firstStep = variantPoint.north();

        Self.move(firstStep);
        final StatusCode result = Self.move(variantPoint);
        if (result != SC_SUCCEEDED) return result;

        final double distance = standardGob.distance(Self.gob());
        System.out.println("succeeded coord: " + variantPoint + ", distance: " + distance);

        return SC_SUCCEEDED;
    }

    static Void test3() {
        final Coord centerPosition = Self.location().tileCenter();

        System.out.println("첫 번째 로그를 선택해주세요");
        Gob firstLog = ClickManager.getGob();

        System.out.println("두 번째 로그를 선택해주세요");
        Gob secondLog = ClickManager.getGob();

        Self.lift(firstLog);
        Self.move(centerPosition.add(- BW_LOG / 2 - BW_BODY / 2, (BH_LOG + BH_BODY) / 2));
        Self.put(centerPosition.add(- BW_LOG / 2 - BW_BODY / 2, 0));

        Self.lift(secondLog);
        Self.move(centerPosition.add(BW_LOG / 2 + BW_BODY / 2, (BH_LOG + BH_BODY) / 2));
        Self.put(centerPosition.add(BW_LOG / 2 + BW_BODY / 2, 0));

        return null;
    }

    static Void getArea() {
        final Rect area = ClickManager.getArea();
        Util.debugPrint(Command.class, "origin: " + area.origin);
        Util.debugPrint(Command.class, "size: " + area.size);
        return null;
    }

    static Void describeGobInArea() {
        System.out.println("click tow points to get area to get gob");
        Array<Gob> gobArray = ClickManager.getGobArrayInArea();
        for (Gob gob : gobArray)
            System.out.println("resource name: " + gob.resourceName());
        return null;
    }
}
