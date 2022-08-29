package lmi.api;

import lmi.*;

public class Self {
    // status code shadow
    private static final Constant.StatusCode SC_SUCCEEDED = Constant.StatusCode.SUCCEEDED;
    private static final Constant.StatusCode SC_INTERRUPTED = Constant.StatusCode.INTERRUPTED;
    private static final Constant.StatusCode SC_FAILED = Constant.StatusCode.FAILED;
    private static final Constant.StatusCode SC_TIME_OUT = Constant.StatusCode.TIME_OUT;

    // command shadow
    private static final String C_MOVE = Constant.Command.Custom.MOVE;

    private static final String A_DIG = Constant.Action.DIG;
    private static final String A_MINE = Constant.Action.MINE;
    private static final String A_CARRY = Constant.Action.CARRY;
    private static final String A_DESTROY = Constant.Action.DESTROY;
    private static final String A_FISH = Constant.Action.FISH;
    private static final String A_INSPECT = Constant.Action.INSPECT;
    private static final String A_REPAIR = Constant.Action.REPAIR;
    private static final String A_CRIME = Constant.Action.CRIME;
    private static final String A_SWIM = Constant.Action.SWIM;
    private static final String A_TRACKING = Constant.Action.TRACKING;
    private static final String A_AGGRO = Constant.Action.AGGRO;
    private static final String A_SHOOT = Constant.Action.SHOOT;

    // access properties
    public static haven.Gob gob() {
        if (lmi.ObjectShadow.mapView() == null)
            return null;

        return lmi.ObjectShadow.mapView().player();
    }

    public static haven.Coord2d location() { return gob().rc; }
    public static double direction() { return gob().a; }
    public static double velocity() { return GobHandler.velocity(gob()); }
    public static haven.Skeleton.Pose pose() { return gob().getpose(); }

    public static haven.Coord locationInCoord() {
        return CoordinateHandler.convertCoord2dToCoord(Self.location());
    }

    public static double hardHitPoint() {
        return haven.LMI.gaugeWidgetGaugeArray(lmi.ObjectShadow.gaugeWidgetArray()[lmi.Constant.Gauge.Index.HIT_POINT])
            .get(lmi.Constant.Gauge.HitPointIndex.HARD)
            .a;
    }

    public static double softHitPoint() {
        return haven.LMI.gaugeWidgetGaugeArray(lmi.ObjectShadow.gaugeWidgetArray()[lmi.Constant.Gauge.Index.HIT_POINT])
            .get(lmi.Constant.Gauge.HitPointIndex.SOFT)
            .a;
    }

    public static double stamina() {
        return haven.LMI.gaugeWidgetGaugeArray(lmi.ObjectShadow.gaugeWidgetArray()[lmi.Constant.Gauge.Index.STAMINA])
            .get(0)
            .a;
    }

    public static double energy() {
        return haven.LMI.gaugeWidgetGaugeArray(lmi.ObjectShadow.gaugeWidgetArray()[lmi.Constant.Gauge.Index.ENERGY])
            .get(0)
            .a;
    }

    // move
    // TODO apply predicted time out value when wait
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED
    public static Constant.StatusCode move(haven.Coord2d point) {
        {
            final Constant.StatusCode result = sendClickMessage_(point);
            lmi.Util.debugPrint(Self.class, "result: " + result);
            if (result != SC_SUCCEEDED) return result;
        }
        return waitEnd_(point);
    }

    // TODO apply predicted time out value when wait
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED
    public static Constant.StatusCode move(haven.Coord point) {
        {
            final Constant.StatusCode result = sendClickMessage_(point);
            if (result != SC_SUCCEEDED) return result;
        }
        return waitEnd_(point);
    }

    // etc
    public static Constant.StatusCode moveNorthTile() {
        haven.Coord2d northTile = CoordinateHandler.northTile(Self.location());
        return move(northTile);
    }

    public static double distance(haven.Gob gob) {
        return Self.location().dist(GobHandler.location(gob));
    }

    public static Constant.StatusCode moveCenter() {
        haven.Coord2d center = CoordinateHandler.tileCenter(Self.location());
        return move(center);
    }

    public static boolean coordinateEquals(haven.Gob gob) {
        if (gob == null)
            return false;

        return CoordinateHandler.equals(Self.location(), GobHandler.location(gob));
    }

    public static boolean coordinateEquals(haven.Coord2d point) {
        if (point == null)
            return false;

        return CoordinateHandler.equals(Self.location(), point);
    }

    public static boolean coordinateEquals(haven.Coord point) {
        if (point == null)
            return false;

        return CoordinateHandler.equals(Self.locationInCoord(), point);
    }

    // TODO implement wait lift
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    public static Constant.StatusCode lift(haven.Gob gob) {
        {
            final Constant.StatusCode result = carry_();
            if (result != SC_SUCCEEDED) return result;
        }
        {
            final Constant.StatusCode result = actClick_(gob);
            if (result != SC_SUCCEEDED) return result;
        }
        return SC_SUCCEEDED;
    }

    // private methods
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED
    private static Constant.StatusCode waitEnd_(haven.Coord2d destination) {
        while (!Self.coordinateEquals(destination)) {
            {
                final Constant.StatusCode result = WaitManager.waitTimeOut(C_MOVE, Constant.TimeOut.FAIL);
                switch (result) {
                    case SUCCEEDED:
                        break;
                    case INTERRUPTED:
                        return SC_INTERRUPTED;
                    case TIME_OUT:
                        lmi.Util.debugPrint(Self.class, "time_out");
                        if (Self.coordinateEquals(destination))
                            return SC_SUCCEEDED;
                        else
                            return SC_FAILED;
                    default:
                        new Exception().printStackTrace();
                        return SC_INTERRUPTED;
                }
            }
        }
        return SC_SUCCEEDED;
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED
    private static Constant.StatusCode waitEnd_(haven.Coord point) {
        final haven.Coord2d destination = CoordinateHandler.convertCoordToCoord2d(point);
        return waitEnd_(destination);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static Constant.StatusCode actClick_(haven.Gob gob) {
        haven.Coord gobLocationInCoord = GobHandler.locationInCoord(gob);
        return WidgetMessageHandler.sendObjectClickMessage(
                ObjectShadow.mapView(),
                Util.mapViewCenter(),
                gobLocationInCoord,
                Constant.Input.Mouse.LEFT,
                Constant.Input.Modifier.NONE,
                Constant.InteractionType.DEFAULT,
                GobHandler.id(gob),
                gobLocationInCoord,
                0,
                Constant.MeshId.NONE);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static Constant.StatusCode dig_() {
        return sendActMessage_(A_DIG);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static Constant.StatusCode mine_() {
        return sendActMessage_(A_MINE);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static Constant.StatusCode carry_() {
        return sendActMessage_(A_CARRY);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static Constant.StatusCode destroy_() {
        return sendActMessage_(A_DESTROY);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static Constant.StatusCode fish_() {
        return sendActMessage_(A_FISH);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static Constant.StatusCode inspect_() {
        return sendActMessage_(A_INSPECT);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static Constant.StatusCode repair_() {
        return sendActMessage_(A_REPAIR);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static Constant.StatusCode crime_() {
        return sendActMessage_(A_CRIME);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static Constant.StatusCode swim_() {
        return sendActMessage_(A_SWIM);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static Constant.StatusCode tracking_() {
        return sendActMessage_(A_TRACKING);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static Constant.StatusCode aggro_() {
        return sendActMessage_(A_AGGRO);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static Constant.StatusCode shoot_() {
        return sendActMessage_(A_SHOOT);
    }

    // send message shadow
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static Constant.StatusCode sendClickMessage_(haven.Coord2d point) {
        return sendClickMessage_(CoordinateHandler.convertCoord2dToCoord(point));
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static Constant.StatusCode sendClickMessage_(haven.Coord point) {
        return WidgetMessageHandler.sendClickMessage(
                ObjectShadow.mapView(),
                Util.mapViewCenter(),
                point,
                lmi.Constant.Input.Mouse.LEFT,
                lmi.Constant.Input.Modifier.NONE);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static Constant.StatusCode sendActMessage_(String action) {
        return WidgetMessageHandler.sendActMessage(ObjectShadow.gameUI().menu, action);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static Constant.StatusCode sendCancelActMessage_() {
        return WidgetMessageHandler.sendCancelActMessage();
    }
}
