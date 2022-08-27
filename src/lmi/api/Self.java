package lmi.api;

import lmi.*;

public class Self {
    // access properties
    public static haven.Gob gob() {
        if (lmi.ObjectShadow.mapView() == null)
            return null;

        return lmi.ObjectShadow.mapView().player();
    }

    public static haven.Coord2d location() {
        return gob().rc;
    }

    public static haven.Coord locationInCoord() {
        return CoordinateHandler.convertCoord2dToCoord(Self.location());
    }

    public static double direction() {
        return gob().a;
    }

    public static haven.Skeleton.Pose pose() {
         return gob().getpose();
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

    public static double velocity() {
        return GobHandler.velocity(gob());
    }

    // move
    private static haven.Coord2d moveDestination_ = null;
    public static boolean move(haven.Coord2d point) {
        boolean result = true;

        move_(point);
        if (!Self.coordinateEquals(moveDestination_))
            result = WaitManager.wait(Constant.Command.Custom.MOVE, Constant.TimeOut.NONE);
        moveDestination_ = null;

        return result;
    }

    public static boolean move(haven.Coord point) {
        boolean result = true;

        move_(point);
        if (!Self.coordinateEquals(moveDestination_))
            result = WaitManager.wait(Constant.Command.Custom.MOVE, -1);
        moveDestination_ = null;

        return result;
    }

    // do action
    public static boolean sendActMessage_(String action) {
        return WidgetMessageHandler.sendActMessage(ObjectShadow.gameUI().menu, action);
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
        carry_();
        haven.Coord2d gobLocation = GobHandler.location(gob);
        haven.Coord gobLocationInCoord = CoordinateHandler.convertCoord2dToCoord(gobLocation);
        return WidgetMessageHandler.actClick(gob);
    }

    public static void notifyIfArrived(haven.Gob gob) {
        if (!Self.coordinateEquals(moveDestination_))
            return;

        WaitManager.notifyIfCommandEquals(Constant.Command.Custom.MOVE);
    }

    // private methods
    private static boolean move_(haven.Coord2d point) {
        moveDestination_ = point;
        return WidgetMessageHandler.move(point);
    }

    private static boolean move_(haven.Coord point) {
        moveDestination_ = CoordinateHandler.convertCoordToCoord2d(point);
        return WidgetMessageHandler.move(point);
    }

    private static void dig_() {
        sendActMessage_(lmi.Constant.Action.DIG);
    }

    private static void mine_() {
        sendActMessage_(lmi.Constant.Action.MINE);
    }

    private static void carry_() {
        sendActMessage_(lmi.Constant.Action.CARRY);
    }

    private static void destroy_() {
        sendActMessage_(lmi.Constant.Action.DESTROY);
    }

    private static void fish_() {
        sendActMessage_(lmi.Constant.Action.FISH);
    }

    private static void inspect_() {
        sendActMessage_(lmi.Constant.Action.INSPECT);
    }

    private static void repair_() {
        sendActMessage_(lmi.Constant.Action.REPAIR);
    }

    private static void crime_() {
        sendActMessage_(lmi.Constant.Action.CRIME);
    }

    private static void swim_() {
        sendActMessage_(lmi.Constant.Action.SWIM);
    }

    private static void tracking_() {
        sendActMessage_(lmi.Constant.Action.TRACKING);
    }

    private static void aggro_() {
        sendActMessage_(lmi.Constant.Action.AGGRO);
    }

    private static void shoot_() {
        sendActMessage_(lmi.Constant.Action.SHOOT);
    }
}
