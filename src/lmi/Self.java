package lmi;

// import haven
import haven.Gob;
import haven.Coord;

// constant
import lmi.Constant.*;
import lmi.Constant.Message;

import static lmi.Constant.ExceptionType.*;
import static lmi.Constant.Message.*;
import static lmi.Constant.Signal.*;
import static lmi.Constant.Action.*;
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

    /// - Throws:
    ///     - ET_MOVE
    public static void move(Coord coord) {
        _sendClickMessage(coord);
        Self.gob().waitMove(coord);
    }

    public static void forceMove(Coord coord, long microseconds) {
        while (true) {
            try {
                Self.move(coord);
                break;
            } catch (LMIException e) {
                if (e.type() != ET_MOVE) throw e;
            }
            Util.sleep(microseconds);
        }
    }

    public static void forceLift(Gob gob, long microseconds) {
        while (true) {
            try {
                Self.lift(gob);
                break;
            } catch (LMIException e) {
                if (e.type() != ET_LIFT) throw e;
            }
            Util.sleep(microseconds);
        }
    }

    // etc
    public static void moveNorth() {
        final Coord north = Self.location().north();
        Self.move(north);
    }

    public static void moveEast() {
        final Coord east = Self.location().east();
        Self.move(east);
    }

    public static void moveWest() {
        final Coord west = Self.location().west();
        Self.move(west);
    }

    public static void moveSouth() {
        final Coord south = Self.location().south();
        Self.move(south);
    }

    public static double distance(Gob gob) {
        return Self.gob().distance(gob);
    }

    public static double distance(Coord coord) {
        return Self.gob().distance(coord);
    }

    public static void moveCenter() {
        final Coord tileCenter = Self.location().tileCenter();
        Self.move(tileCenter);
    }

    // carry
    /// - Throws:
    ///     - ET_LIFT
    public static void lift(Gob gob) {
        _sendCarryMessage();
        WidgetMessageHandler.actionClick(gob);
        try {
            Self.gob().waitMove();
        } catch (LMIException e) { if (e.type() == ET_INTERRUPTED) throw e; }
        Self.gob().waitLift(gob);
    }

    /// - Throws:
    ///     - ET_PUT
    public static void put(Coord coord) {
        _put(coord);
        try {
            Self.gob().waitMove();
        } catch (LMIException e) { if (e.type() == ET_INTERRUPTED) throw e; }
        Self.gob().waitPut();
    }

    private static boolean _isCursorChanged(String cursor) {
        return WidgetManager.cursor().get().name.endsWith(cursor);
    }

    private static void _dig() {
        Self._sendActionMessage(A_DIG);
    }

    private static void _mine() {
        Self._sendActionMessage(A_MINE);
    }

    private static void _sendCarryMessage() {
        Self._sendActionMessage(A_CARRY);
    }

    private static void _destroy() {
        Self._sendActionMessage(A_DESTROY);
    }

    private static void _fish() {
        Self._sendActionMessage(A_FISH);
    }

    private static void _inspect() {
        Self._sendActionMessage(A_INSPECT);
    }

    private static void _repair() {
        Self._sendActionMessage(A_REPAIR);
    }

    private static void _crime() {
        Self._sendActionMessage(A_CRIME);
    }

    private static void _swim() {
        Self._sendActionMessage(A_SWIM);
    }

    private static void _tracking() {
        Self._sendActionMessage(A_TRACKING);
    }

    private static void _aggro() {
        Self._sendActionMessage(A_AGGRO);
    }

    private static void _shoot() {
        Self._sendActionMessage(A_SHOOT);
    }

    // send message shadow
    private static void _sendClickMessage(Coord coord) {
        WidgetMessageHandler.sendClickMessage(
                ObjectShadow.mapView(),
                Util.mapViewCenter(),
                coord,
                IM_LEFT,
                IM_NONE);
    }

    private static void _sendActionMessage(String action) {
        WidgetMessageHandler.sendActionMessage(WidgetManager.menuGrid(), action);
    }

    private static void _put(Coord coord) {
        WidgetMessageHandler.put(coord);
    }

    private static void _sendCancelActionMessage() {
        WidgetMessageHandler.sendCancelActionMessage();
    }
}
