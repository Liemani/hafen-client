package lmi.api;

import lmi.*;

public class Self {
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
    private static haven.Coord2d moveDestination_ = null;
    // TODO apply predicted time out value when wait
    public static boolean move(haven.Coord2d point) {
        moveDestination_ = point;
        if (!move_(point)) return false;
        while (!Self.coordinateEquals(moveDestination_))
            if (!WaitManager.waitTimeOut(Constant.Command.Custom.MOVE, Constant.TimeOut.FAIL))
                return false;
        moveDestination_ = null;
        return true;
    }

    // TODO apply predicted time out value when wait
    public static boolean move(haven.Coord point) {
        moveDestination_ = CoordinateHandler.convertCoordToCoord2d(point);
        if (!move_(point)) return false;
        while (!Self.coordinateEquals(moveDestination_))
            if (!WaitManager.waitTimeOut(Constant.Command.Custom.MOVE, Constant.TimeOut.FAIL))
                return false;
        moveDestination_ = null;
        return true;
    }

    // etc
    public static void moveNorthTile() throws InterruptedException {
        haven.Coord2d northTile = CoordinateHandler.northTile(Self.location());
        move(northTile);
    }

    public static double distance(haven.Gob gob) {
        return Self.location().dist(GobHandler.location(gob));
    }

    public static void moveCenter() throws InterruptedException {
        haven.Coord2d center = CoordinateHandler.tileCenter(Self.location());
        move(center);
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

    public static boolean lift(haven.Gob gob) {
        if (!carry_()) return false;
        final boolean succeeded = actClick_(gob);
        if (!succeeded)
            sendCancelActMessage_();
        return succeeded;
    }

    // private methods
    private static boolean move_(haven.Coord2d point) {
        return sendClickMessage_(point);
    }

    private static boolean move_(haven.Coord point) {
        return sendClickMessage_(point);
    }

    private static boolean sendClickMessage_(haven.Coord2d point) {
        return sendClickMessage_(CoordinateHandler.convertCoord2dToCoord(point));
    }

    private static boolean sendClickMessage_(haven.Coord point) {
        return WidgetMessageHandler.sendClickMessage(
                ObjectShadow.mapView(),
                Util.mapViewCenter(),
                point,
                lmi.Constant.Input.Mouse.LEFT,
                lmi.Constant.Input.Modifier.NONE);
    }

    private static boolean sendActMessage_(String action) {
        return WidgetMessageHandler.sendActMessage(ObjectShadow.gameUI().menu, action);
    }

    private static boolean actClick_(haven.Gob gob) {
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

    private static boolean sendCancelActMessage_() {
        return WidgetMessageHandler.sendCancelActMessage();
    }

    private static boolean dig_() {
        return sendActMessage_(lmi.Constant.Action.DIG);
    }

    private static boolean mine_() {
        return sendActMessage_(lmi.Constant.Action.MINE);
    }

    private static boolean carry_() {
        return sendActMessage_(lmi.Constant.Action.CARRY);
    }

    private static boolean destroy_() {
        return sendActMessage_(lmi.Constant.Action.DESTROY);
    }

    private static boolean fish_() {
        return sendActMessage_(lmi.Constant.Action.FISH);
    }

    private static boolean inspect_() {
        return sendActMessage_(lmi.Constant.Action.INSPECT);
    }

    private static boolean repair_() {
        return sendActMessage_(lmi.Constant.Action.REPAIR);
    }

    private static boolean crime_() {
        return sendActMessage_(lmi.Constant.Action.CRIME);
    }

    private static boolean swim_() {
        return sendActMessage_(lmi.Constant.Action.SWIM);
    }

    private static boolean tracking_() {
        return sendActMessage_(lmi.Constant.Action.TRACKING);
    }

    private static boolean aggro_() {
        return sendActMessage_(lmi.Constant.Action.AGGRO);
    }

    private static boolean shoot_() {
        return sendActMessage_(lmi.Constant.Action.SHOOT);
    }
}
