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
import static lmi.Constant.Plob.*;
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
            } catch (LMIException e) { if (e.type() != ET_MOVE) throw e; }
            Api.sleep(TO_RETRY);
        }
        throw new LMIException(ET_MOVE);
    }

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
        } catch (LMIException e) { if (e.type() != ET_MOVE) throw e; }
        Self.gob().waitLift(gob);
    }

    /// - Throws:
    ///     - ET_LIFT
    public static void forceLift(Gob gob) {
        for (int retry = 0; retry < RETRY_MAX; ++retry) {
            try {
                Api.lift(gob);
                return;
            } catch (LMIException e) { if (e.type() != ET_LIFT) throw e; }
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
            } catch (LMIException e) { if (e.type() != ET_PUT) throw e; }
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
        } catch (LMIException e) { if (e.type() != ET_MOVE) throw e; }
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

    public static Rect getArea() { return ClickManager.getArea(); }

    public static Array<Gob> gobArrayIn(Rect area) {
        return Api.gobArrayWhere(gob -> area.contains(gob.location()));
    }

    public static Array<Gob> getGobArrayInArea() {
        final Rect area = ClickManager.getArea();
        return Api.gobArrayIn(area);
    }

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

    /// - Throws:
    ///     - ET_WINDOW_OPEN
    public static void prepareBuild(String plobName, Coord location, int direction, String title) {
        WidgetManager.menuGrid().wdgmsg(M_ACT, A_BP, plobName, 0);
        WidgetMessageHandler.sendPlaceMessage(ObjectShadow.mapView(), location, direction, IM_LEFT, IM_NONE);
        try {
            Self.gob().waitMove();
        } catch (LMIException e) { if (e.type() != ET_MOVE) throw e; }
        ObjectShadow.gameUI().waitWindowAdded(title);
    }

    // ISBox
    public static void transferItem() { WidgetManager.isbox().wdgmsg(M_XFER); }
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
