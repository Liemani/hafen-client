package lmi.api;

import lmi.*;
import lmi.Constant.*;

public class Self {
    // status code shadow
    private static final StatusCode SC_SUCCEEDED = StatusCode.SUCCEEDED;
    private static final StatusCode SC_INTERRUPTED = StatusCode.INTERRUPTED;
    private static final StatusCode SC_FAILED = StatusCode.FAILED;
    private static final StatusCode SC_TIME_OUT = StatusCode.TIME_OUT;

    // custom command shadow
    private static final Command.Custom C_SELF_MOVE_DID_STARTED = Command.Custom.SELF_MOVE_DID_STARTED;
    private static final Command.Custom C_SELF_MOVE_DID_ENDED = Command.Custom.SELF_MOVE_DID_ENDED;

    private static final String A_DIG = Action.DIG;
    private static final String A_MINE = Action.MINE;
    private static final String A_CARRY = Action.CARRY;
    private static final String A_DESTROY = Action.DESTROY;
    private static final String A_FISH = Action.FISH;
    private static final String A_INSPECT = Action.INSPECT;
    private static final String A_REPAIR = Action.REPAIR;
    private static final String A_CRIME = Action.CRIME;
    private static final String A_SWIM = Action.SWIM;
    private static final String A_TRACKING = Action.TRACKING;
    private static final String A_AGGRO = Action.AGGRO;
    private static final String A_SHOOT = Action.SHOOT;

    // access properties
    public static haven.Gob gob() {
        if (ObjectShadow.mapView() == null)
            return null;

        return ObjectShadow.mapView().player();
    }

    public static haven.Coord2d location() { return gob().rc; }
    public static double direction() { return gob().a; }
    public static double velocity() { return GobHandler.velocity(gob()); }
    public static haven.Skeleton.Pose pose() { return gob().getpose(); }

    public static haven.Coord locationInCoord() {
        return CoordinateHandler.convertCoord2dToCoord(Self.location());
    }

    public static double hardHitPoint() {
        return haven.LMI.gaugeWidgetGaugeArray(ObjectShadow.gaugeWidgetArray()[Gauge.Index.HIT_POINT])
            .get(Gauge.HitPointIndex.HARD)
            .a;
    }

    public static double softHitPoint() {
        return haven.LMI.gaugeWidgetGaugeArray(ObjectShadow.gaugeWidgetArray()[Gauge.Index.HIT_POINT])
            .get(Gauge.HitPointIndex.SOFT)
            .a;
    }

    public static double stamina() {
        return haven.LMI.gaugeWidgetGaugeArray(ObjectShadow.gaugeWidgetArray()[Gauge.Index.STAMINA])
            .get(0)
            .a;
    }

    public static double energy() {
        return haven.LMI.gaugeWidgetGaugeArray(ObjectShadow.gaugeWidgetArray()[Gauge.Index.ENERGY])
            .get(0)
            .a;
    }

    // move
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED
    public static StatusCode move(haven.Coord2d point) {
        {
            final StatusCode result = sendClickMessage_(point);
            if (result != SC_SUCCEEDED) return result;
        }
        return waitMove_(point);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED
    public static StatusCode move(haven.Coord point) {
        {
            final StatusCode result = sendClickMessage_(point);
            if (result != SC_SUCCEEDED) return result;
        }
        return waitMove_(point);
    }

    // etc
    public static StatusCode moveNorthTile() {
        haven.Coord2d northTile = CoordinateHandler.northTile(Self.location());
        return move(northTile);
    }

    public static double distance(haven.Gob gob) {
        return Self.location().dist(GobHandler.location(gob));
    }

    public static StatusCode moveCenter() {
        haven.Coord2d center = CoordinateHandler.tileCenter(Self.location());
        return move(center);
    }

    public static boolean isAt(haven.Coord2d point) {
        if (point == null)
            return false;

        return CoordinateHandler.equals(Self.location(), point);
    }

    public static boolean isAt(haven.Coord point) {
        if (point == null)
            return false;

        return CoordinateHandler.equals(Self.locationInCoord(), point);
    }

    // TODO implement wait lift
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    public static StatusCode lift(haven.Gob gob) {
        {
            final StatusCode result = carry_();
            if (result != SC_SUCCEEDED) return result;
        }
        {
            final StatusCode result = actClick_(gob);
            if (result != SC_SUCCEEDED) return result;
        }
        return SC_SUCCEEDED;
    }

    // private methods
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED
    private static StatusCode waitMove_(haven.Coord2d destination) {
        if (Self.isAt(destination))
            return SC_SUCCEEDED;
        if (!isMoving_()) {
            final StatusCode result = waitMoveStarting_();
            if (result != SC_SUCCEEDED) return result;
        }
        if (waitMoveEnding_() == SC_INTERRUPTED) return SC_INTERRUPTED;
        if (Self.isAt(destination))
            return SC_SUCCEEDED;
        else
            return SC_FAILED;
    }

    // private methods
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED
    private static StatusCode waitMove_(haven.Coord destination) {
        while (!Self.isAt(destination)) {
            if (!isMoving_()) {
                final StatusCode result = waitMoveStarting_();
                if (result != SC_SUCCEEDED) return result;
            }
            if (waitMoveEnding_() == SC_INTERRUPTED) return SC_INTERRUPTED;
        }
        return SC_SUCCEEDED;
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED
    private static StatusCode waitMoveStarting_() {
        if (isMoving_()) return SC_SUCCEEDED;
        final StatusCode result = WaitManager.waitTimeOut(C_SELF_MOVE_DID_STARTED, TimeOut.TEMPORARY);
        switch (result) {
            case SUCCEEDED: return SC_SUCCEEDED;
            case INTERRUPTED: return SC_INTERRUPTED;
            case TIME_OUT: return isMoving_() ? SC_SUCCEEDED : SC_FAILED;
            default:
                new Exception().printStackTrace();
                return SC_INTERRUPTED;
        }
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode waitMoveEnding_() {
        while (true) {
            if (!isMoving_()) return SC_SUCCEEDED;
            final StatusCode result = WaitManager.waitTimeOut(C_SELF_MOVE_DID_ENDED, TimeOut.GENERAL);
            switch (result) {
                case SUCCEEDED: return SC_SUCCEEDED;
                case INTERRUPTED: return SC_INTERRUPTED;
                case TIME_OUT:
                    if (isMoving_())
                        break;
                    else
                        return SC_SUCCEEDED;
                default:
                    new Exception().printStackTrace();
                    return SC_INTERRUPTED;
            }
        }
    }

    private static boolean isMoving_() {
        haven.GAttrib attribute = Self.gob().getattr(haven.Moving.class);
        return attribute != null;
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode actClick_(haven.Gob gob) {
        haven.Coord gobLocationInCoord = GobHandler.locationInCoord(gob);
        return WidgetMessageHandler.sendObjectClickMessage(
                ObjectShadow.mapView(),
                Util.mapViewCenter(),
                gobLocationInCoord,
                Input.Mouse.LEFT,
                Input.Modifier.NONE,
                InteractionType.DEFAULT,
                GobHandler.id(gob),
                gobLocationInCoord,
                0,
                MeshId.NONE);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode dig_() {
        return sendActMessage_(A_DIG);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode mine_() {
        return sendActMessage_(A_MINE);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode carry_() {
        return sendActMessage_(A_CARRY);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode destroy_() {
        return sendActMessage_(A_DESTROY);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode fish_() {
        return sendActMessage_(A_FISH);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode inspect_() {
        return sendActMessage_(A_INSPECT);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode repair_() {
        return sendActMessage_(A_REPAIR);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode crime_() {
        return sendActMessage_(A_CRIME);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode swim_() {
        return sendActMessage_(A_SWIM);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode tracking_() {
        return sendActMessage_(A_TRACKING);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode aggro_() {
        return sendActMessage_(A_AGGRO);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode shoot_() {
        return sendActMessage_(A_SHOOT);
    }

    // send message shadow
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode sendClickMessage_(haven.Coord2d point) {
        return sendClickMessage_(CoordinateHandler.convertCoord2dToCoord(point));
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode sendClickMessage_(haven.Coord point) {
        return WidgetMessageHandler.sendClickMessage(
                ObjectShadow.mapView(),
                Util.mapViewCenter(),
                point,
                Input.Mouse.LEFT,
                Input.Modifier.NONE);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode sendActMessage_(String action) {
        return WidgetMessageHandler.sendActMessage(ObjectShadow.gameUI().menu, action);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode sendCancelActMessage_() {
        return WidgetMessageHandler.sendCancelActMessage();
    }
}
