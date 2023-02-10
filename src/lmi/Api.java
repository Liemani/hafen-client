package lmi;

import java.util.function.Predicate;

import haven.*;

import lmi.*;
import static lmi.Constant.*;
import static lmi.Constant.Input.Mouse.*;
import static lmi.Constant.Input.Modifier.*;
import static lmi.Constant.ExceptionType.*;
import static lmi.Constant.TimeOut.*;
import static lmi.Constant.Action.*;
import static lmi.Constant.Plan.*;
import static lmi.Constant.WindowTitle.*;
import static lmi.Constant.Message.*;

public class Api {
    public static void interact(Gob gob) {
        WidgetMessageHandler.click(gob, IM_RIGHT, IM_NONE);
    }

    /// - Throws:
    ///     - ET_MOVE
    public static void move(Coord coord) {
        _sendMoveMessage(coord);
        Self.gob().waitMove(coord);
    }

    /// - Throws:
    ///     - ET_MOVE
    public static void forceMove(Coord coord) {
        for (int retry = 0; retry < RETRY_MAX; ++retry) {
            try {
                Api.move(coord);
                return;
            } catch (LMIException e) { if (e.type != ET_MOVE) throw e; }
            Api.sleep(TO_RETRY);
        }
        throw new LMIException(ET_MOVE);
    }

    /// - Throws:
    ///     - ET_MOVE
    ///     - ET_NO_PATH
    public static void pathfindMove(Gob gob) { Pathfinder.move(gob); }
    public static void pathfindMove(Coord coord) { Pathfinder.move(coord); }

    /// - Throws:
    ///     - ET_MOVE
    ///     - ET_NO_PATH
    public static Gob pathfindMove(Array<Gob> gobArray) {
        for (Gob gob : gobArray) {
            try {
                Api.pathfindMove(gob);
                return gob;
            } catch (LMIException e) {
                if (e.type == ET_NO_PATH) continue;
                else throw e;
            }
        }
        throw new LMIException(ET_NO_PATH);
    }

    /// - Throws:
    ///     - ET_MOVE
    ///     - ET_NO_PATH
    ///     - ET_NO_INPUT
    public static void pathfindTakeItemFromStockpileArray(Array<Gob> stockpileArray, int count) {
        for (Gob stockpile : stockpileArray) {
            try {
                count -= Api.pathfindTakeItemFromStockpile(stockpile, count);
                if (count == 0) return;
            } catch (LMIException e) {
                if (e.type == ET_NO_PATH) continue;
                else throw e;
            }
        }

        if (stockpileArray.count() == 0)
            throw new LMIException(ET_NO_INPUT);
        else
            throw new LMIException(ET_NO_PATH);
    }

    /// - Throws:
    ///     - ET_MOVE
    ///     - ET_NO_PATH
    ///     - ET_NO_INPUT
    public static void pathfindTakeItemFromContainerArray(Array<Gob> containerArray, String[] nameSet, int count) {
        for (Gob container : containerArray) {
            try {
                count -= pathfindTakeItemFromContainer(container, nameSet, count);
                if (count == 0) return;
            } catch (LMIException e) {
                if (e.type == ET_NO_PATH) continue;
                else throw e;
            }
        }
        if (containerArray.count() == 0)
            throw new LMIException(ET_NO_INPUT);
        else
            throw new LMIException(ET_NO_PATH);
    }

    /// - Throws:
    ///     - ET_MOVE
    ///     - ET_NO_PATH
    public static int pathfindTakeItemFromStockpile(Gob stockpile, int count) {
        Pathfinder.move(stockpile);
        return Api.takeItemFromStockpile(stockpile, count);
    }

    /// - Throws:
    ///     - ET_MOVE
    ///     - ET_NO_PATH
    public static int pathfindTakeItemFromContainer(Gob container, String[] nameSet, int count) {
        final Window window = Api.openGobWindow(container);
        return takeItemFromContainer(container, nameSet, count);
    }

    /// - Throws:
    ///     - ET_MOVE
    ///     - ET_WINDOW_OPEN
    public static int takeItemFromStockpile(Gob stockpile, int count) {
        final Window window = Api.openGobWindow(stockpile);
        return Api.takeItemFromIsbox(window.getChildOf(ISBox.class), count);
    }

    /// - Throws:
    ///     - ET_MOVE
    ///     - ET_WINDOW_OPEN
    public static int takeItemFromContainer(Gob container, String[] genericArray, int count) {
        final Window window = Api.openGobWindow(container);
        final Inventory inventory = window.getChildOf(Inventory.class);
        Widget child = inventory.child;
        int transferedItemCount = 0;
        while (child != null) {
            if (child instanceof GItem) {
                final GItem item = (GItem)child;
                for (String element : genericArray) {
                    if (item.isResourceNameEndsWith(element)) {
                        item.transfer();
                        ++transferedItemCount;
                        if (transferedItemCount == count)
                            return count;
                        break;
                    }
                }
            }
            child = child.next;
        }
        return transferedItemCount;
    }

    // Plan object
    static void planObject(String planName) {
        WidgetManager.menuGrid().sendMessage(M_ACT, A_BP, planName, 0);
        while (!ObjectShadow.mapView().isPlanningObject())
            Api.sleep(TO_RETRY);
    }

    /// - Throws:
    ///     - ET_DECIDE_PLAN
    public static Window planAndDecideObject(String planName, Coord location, int direction) {
        Api.planObject(planName);
        ErrorMessageManager.clear();
        Api.decidePlan(location, direction);
        for (int retry = 0; retry < RETRY_MAX; ++retry) {
            final Window window = WidgetManager.window();
            if (window != null)
                return window;
            else {
                Api.sleep(TO_RETRY);
                if (ErrorMessageManager.message() != null)
                    Api.decidePlan(location, direction);
            }
        }
        if (ObjectShadow.mapView().isPlanningObject())
            WidgetMessageHandler.sendCancelPlanMessage();
        throw new LMIException(ET_DECIDE_PLAN);
    }

    public static void decidePlan(Coord location, int direction) {
        WidgetMessageHandler.sendPlaceMessage(location, direction, IM_LEFT, IM_NONE);
    }

    /// - Throws:
    ///     - ET_DECIDE_PLAN
    public static void build(String planName, Coord location, int direction) {
        final Window window = planAndDecideObject(planName, location, direction);
        final Button button = window.getChildOf(Button.class);
        WidgetMessageHandler.sendButtonBuildMessage(button);
        Self.gob().waitBuild();
    }

    // Interact Gob
    /// Interact with stockpile or container
    public static Window openGobWindow(Gob gob) {
        while (true) {
            Api.interact(gob);
            final Window window = WidgetManager.window();
            if (window != null)
                return window;
            else
                Api.sleep(TO_RETRY);
        }
    }

    /// - Return: count of item taken
    public static int takeItemFromIsbox(ISBox isbox, int count) {
        final int transferCount = Math.min(count, isbox.count());
        for (int i = 0; i < transferCount; ++i)
            isbox.transfer();
        return transferCount;
    }

    /// - Throws:
    ///     - ET_MOVE
    public static void moveNorth() { Api.move(Self.location().north()); }
    public static void moveEast() { Api.move(Self.location().east()); }
    public static void moveWest() { Api.move(Self.location().west()); }
    public static void moveSouth() { Api.move(Self.location().south()); }
    public static void moveCenter() { Api.move(Self.location().center()); }

    /// - Throws:
    ///     - ET_LIFT
    public static void lift(Gob gob) {
        WidgetManager.menuGrid().wdgmsg(M_ACT, A_CARRY, 0);
        _sendObjectClickMessage(gob);
        try {
            Self.gob().waitMove();
        } catch (LMIException e) { if (e.type != ET_MOVE) throw e; }
        Self.gob().waitLift(gob);
    }

    /// - Throws:
    ///     - ET_LIFT
    public static void forceLift(Gob gob) {
        for (int retry = 0; retry < RETRY_MAX; ++retry) {
            try {
                Api.lift(gob);
                return;
            } catch (LMIException e) { if (e.type != ET_LIFT) throw e; }
            Api.sleep(TO_RETRY);
        }
        throw new LMIException(ET_LIFT);
    }

    /// - Throws:
    ///     - ET_PUT
    public static void forcePut(Coord coord) {
        for (int retry = 0; retry < RETRY_MAX; ++retry) {
            try {
                Api.put(coord);
                return;
            } catch (LMIException e) { if (e.type != ET_PUT) throw e; }
            Api.sleep(TO_RETRY);
        }
        throw new LMIException(ET_PUT);
    }

    /// - Throws:
    ///     - ET_PUT
    public static void put(Coord coord) {
        _sendPutMessage(coord);
        try {
            Self.gob().waitMove();
        } catch (LMIException e) { if (e.type != ET_MOVE) throw e; }
        Self.gob().waitPut();
    }

    // Get Gob
    public static Gob closestGobIn(Array<Gob> gobArray) {
        Gob closestGob = null;
        double distanceToClosestGob = Double.MAX_VALUE;

        for (Gob gob : gobArray) {
            final double distance = Self.distance(gob);
            if (distance < distanceToClosestGob) {
                closestGob = gob;
                distanceToClosestGob = distance;
            }
        }

        return closestGob;
    }

    public static Gob closestGob() {
        final Array<Gob> gobArray = Api.gobArrayWhere(gob -> gob != Self.gob());
        return Api.closestGobIn(gobArray);
    }

    public static Gob closestGobOf(String name) {
        final Array<Gob> gobArray = Api.gobArrayWhere(gob -> gob.resourceName().endsWith(name));
        return Api.closestGobIn(gobArray);
    }

    public static Gob getGob() { return ClickManager.getGob(); }

    // Get Gob Array
    public static Array<Gob> gobArray() {
        Array<Gob> gobArray = new Array<Gob>();

        for (Gob gob : ObjectShadow.objectCache().gobArray())
            if (gob.resource() != null)
                gobArray.append(gob);

        return gobArray;
    }

    public static Array<Gob> gobArrayWhere(Predicate<Gob> predicate) {
        Array<Gob> gobArray = new Array<Gob>();

        for (Gob gob : ObjectShadow.objectCache().gobArray()) {
            if (gob.resource() == null) continue;
            else if (predicate.test(gob)) gobArray.append(gob);
        }
        return gobArray;
    }

    public static Array<Gob> gobArrayIn(Rect area) {
        return Api.gobArrayWhere(gob -> area.contains(gob.location()));
    }

    public static Array<Gob> getGobArrayInArea() {
        final Rect area = ClickManager.getArea();
        return Api.gobArrayIn(area);
    }

    // Get Area
    public static Rect getArea() { return ClickManager.getArea(); }

    // Print Message to Console
    public static void error(String message) { ObjectShadow.gameUI().error(message); }
    public static void alert(String message) { ObjectShadow.gameUI().alert(message); }
    public static void message(String message) { ObjectShadow.ui().cons.out.println(message); }

    // Etc
    public static void sleep(long microseconds) {
        try {
            Thread.sleep(microseconds);
        } catch (InterruptedException e) {
            throw new LMIException(ET_INTERRUPTED);
        }
    }

    // ISBox
    public static void pressButton() { WidgetManager.button().sendMessage(M_ACTIVATE); }

    // Inventory
    public static Inventory inventory() { return WidgetManager.inventory(); }

    public static void transferItem(Array<GItem> itemArray) {
        for (GItem item : itemArray)
            item.wdgmsg(M_TRANSFER, Coord.zero(), IM_LEFT);
    }

//      public static Array<GItem> getItemArray(String name, int count) {
//          // TODO fix this with considering real implementation of BuildDryingFrame
//          Widget child = WidgetManager.inventory().child;
//          while (count != 0 && child != null) {
//              if (child instanceof GItem) {
//                  child.wdgmsg("transfer", Coord.zero(), IM_LEFT);
//                  --count;
//              }
//              child = child.next;
//          }
//          return new Array<GItem>();
//      }

    // Private Method
    private static void _sendMoveMessage(Coord coord) {
        WidgetMessageHandler.click(coord, IM_LEFT, IM_NONE);
    }

    private static void _sendObjectClickMessage(Gob gob) {
        final Coord gobLocationInCoord = gob.location();
        WidgetMessageHandler.click(gob, IM_LEFT, IM_NONE);
    }

    private static void _sendPutMessage(Coord location) {
        WidgetMessageHandler.click(location, IM_RIGHT, IM_NONE);
    }
}
