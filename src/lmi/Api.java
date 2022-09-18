package lmi;

import java.util.function.Predicate;

import haven.Gob;
import haven.Coord;

import lmi.*;
import static lmi.Constant.Input.Mouse.*;
import static lmi.Constant.Input.Modifier.*;
import static lmi.Constant.ExceptionType.*;
import static lmi.Constant.TimeOut.*;
import static lmi.Constant.Action.*;
import static lmi.Constant.Plob.*;
import static lmi.Constant.WindowTitle.*;
import static lmi.Constant.Message.*;

public class Api {
    /// - Throws:
    ///     - ET_MOVE
    public static void move(Coord coord) {
        _sendMoveMessage(coord);
        Self.gob().waitMove(coord);
    }

    public static void forceMove(Coord coord) {
        while (true) {
            try {
                Api.move(coord);
                break;
            } catch (LMIException e) {
                if (e.type() != ET_MOVE) throw e;
            }
            Api.sleep(TO_RETRY);
        }
    }

    public static void moveNorth() { Api.move(Self.location().north()); }
    public static void moveEast() { Api.move(Self.location().east()); }
    public static void moveWest() { Api.move(Self.location().west()); }
    public static void moveSouth() { Api.move(Self.location().south()); }
    public static void moveCenter() { Api.move(Self.location().tileCenter()); }

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

    public static void forceLift(Gob gob) {
        while (true) {
            try {
                Api.lift(gob);
                break;
            } catch (LMIException e) {
                if (e.type() != ET_LIFT) throw e;
            }
            Api.sleep(TO_RETRY);
        }
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
            if (gob.getClass() != Gob.class) continue;
            else if (predicate.test(gob)) gobArray.append(gob);
        }
        return gobArray;
    }

    public static Rect getArea() { return ClickManager.getArea(); }

    public static Array<Gob> gobArrayIn(Rect area) {
        return gobArrayWhere(gob -> area.contains(gob.location()));
    }

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

    static Array<Gob> getGobArrayInArea() {
        final Rect area = ClickManager.getArea();
        return gobArrayIn(area);
    }

    // Print Message to haven.Console
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

    // Private Method
    private static void _sendMoveMessage(Coord coord) {
        WidgetMessageHandler.click(coord, IM_LEFT, IM_NONE);
    }

    private static void _sendObjectClickMessage(Gob gob) {
        final Coord gobLocationInCoord = gob.location();
        WidgetMessageHandler.click(gob, IM_LEFT, IM_NONE);
    }

    public static void _sendPutMessage(Coord location) {
        WidgetMessageHandler.click(location, IM_RIGHT, IM_NONE);
    }
}
