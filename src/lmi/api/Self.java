package lmi.api;

import haven.Gob;
import haven.Coord;

import lmi.*;

// constant
import lmi.Constant.*;
import lmi.Constant.StatusCode;
import lmi.Constant.Action;

import static lmi.Constant.StatusCode.*;
import static lmi.Constant.Action.*;
import static lmi.Constant.Action.Custom.*;
import static lmi.Constant.SelfAction.*;
import static lmi.Constant.Input.Mouse.*;
import static lmi.Constant.Input.Modifier.*;
import static lmi.Constant.InteractionType.*;
import static lmi.Constant.MeshId.*;
import static lmi.Constant.TimeOut.*;
import static lmi.Constant.Gauge.Index.*;
import static lmi.Constant.Gauge.HitPointIndex.*;

// resource
import static lmi.Constant.gfx.borka.*;
import static lmi.Constant.gfx.hud.curs.*;

public class Self {
    // access properties
    public static Gob gob() {
        if (ObjectShadow.mapView() == null) return null;
        return ObjectShadow.mapView().player();
    }

    public static Coord location() { return Self.gob().location(); }
    public static double direction() { return Self.gob().direction(); }
    public static double velocity() { return Self.gob().velocity(); }
    public static haven.Skeleton.Pose pose() { return Self.gob().getpose(); }
    public static boolean hasPose(String poseName) { return Self.gob().hasPose(poseName); }

    public static double hardHitPoint() {
        return haven.LMI.gaugeWidgetGaugeArray(ObjectShadow.gaugeWidgetArray()[GI_HIT_POINT])
            .get(GI_HARD)
            .a;
    }

    public static double softHitPoint() {
        return haven.LMI.gaugeWidgetGaugeArray(ObjectShadow.gaugeWidgetArray()[GI_HIT_POINT])
            .get(GI_SOFT)
            .a;
    }

    public static double stamina() {
        return haven.LMI.gaugeWidgetGaugeArray(ObjectShadow.gaugeWidgetArray()[GI_STAMINA])
            .get(0)
            .a;
    }

    public static double energy() {
        return haven.LMI.gaugeWidgetGaugeArray(ObjectShadow.gaugeWidgetArray()[GI_ENERGY])
            .get(0)
            .a;
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED_MOVE
    public static StatusCode move(Coord coord) {
        if (sendClickMessage_(coord) == SC_INTERRUPTED) return SC_INTERRUPTED;
        return Self.gob().waitMove(coord);
    }

    // etc
    public static StatusCode moveNorth() {
        Coord north = Self.location().north();
        return move(north);
    }

    public static StatusCode moveEast() {
        Coord east = Self.location().east();
        return move(east);
    }

    public static StatusCode moveWest() {
        Coord west = Self.location().west();
        return move(west);
    }

    public static StatusCode moveSouth() {
        Coord south = Self.location().south();
        return move(south);
    }

    public static double distance(Gob gob) {
        return Self.gob().distance(gob);
    }

    public static StatusCode moveCenter() {
        Coord tileCenter = Self.location().tileCenter();
        return move(tileCenter);
    }

    // carry
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED_LIFT
    public static StatusCode lift(Gob gob) {
        if (sendCarryMessage_() == SC_INTERRUPTED) return SC_INTERRUPTED;
        if (waitCursorChange_(RN_HAND) == SC_INTERRUPTED) return SC_INTERRUPTED;
        if (WidgetMessageHandler.actionClick(gob) == SC_INTERRUPTED) return SC_INTERRUPTED;
        Self.gob().waitMove();
        return Self.gob().waitLift(gob);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED_PUT
    public static StatusCode put(Coord coord) {
        if (put_(coord) == SC_INTERRUPTED) return SC_INTERRUPTED;
        Self.gob().waitMove();
        return Self.gob().waitPut();
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode waitCursorChange_(String cursor) {
        while (true) {
            if (isCursorChanged_(cursor)) return SC_SUCCEEDED;
            switch (WaitManager.waitTimeOut(A_CHANGE_CURSOR, TO_TEMPORARY)) {
                case SC_SUCCEEDED: return SC_SUCCEEDED;
                case SC_INTERRUPTED: return SC_INTERRUPTED;
                case SC_TIME_OUT: break;
                default:
                    new Exception().printStackTrace();
                    return SC_INTERRUPTED;
            }
        }
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static boolean isCursorChanged_(String cursor) {
        return WidgetManager.cursor().get().name.endsWith(cursor);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode dig_() {
        return sendActionMessage_(A_DIG);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode mine_() {
        return sendActionMessage_(A_MINE);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode sendCarryMessage_() {
        return sendActionMessage_(A_CARRY);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode destroy_() {
        return sendActionMessage_(A_DESTROY);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode fish_() {
        return sendActionMessage_(A_FISH);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode inspect_() {
        return sendActionMessage_(A_INSPECT);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode repair_() {
        return sendActionMessage_(A_REPAIR);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode crime_() {
        return sendActionMessage_(A_CRIME);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode swim_() {
        return sendActionMessage_(A_SWIM);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode tracking_() {
        return sendActionMessage_(A_TRACKING);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode aggro_() {
        return sendActionMessage_(A_AGGRO);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode shoot_() {
        return sendActionMessage_(A_SHOOT);
    }

    // send message shadow
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode sendClickMessage_(Coord coord) {
        return WidgetMessageHandler.sendClickMessage(
                ObjectShadow.mapView(),
                Util.mapViewCenter(),
                coord,
                IM_LEFT,
                IM_NONE);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode sendActionMessage_(String action) {
        return WidgetMessageHandler.sendActionMessage(WidgetManager.menuGrid(), action);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode put_(Coord coord) {
        return WidgetMessageHandler.put(coord);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode sendCancelActionMessage_() {
        return WidgetMessageHandler.sendCancelActionMessage();
    }
}
