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

    public static void notifyIfArrived(haven.Gob gob) {
        if (!Self.coordinateEquals(moveDestination_))
            return;

        WaitManager.notifyIfCommandEquals(Constant.Command.Custom.MOVE);
    }

    public static void clickIntCoordinate(haven.Coord point) {
        WidgetMessageHandler.click(
                lmi.ObjectShadow.mapView(),
                Util.mapViewCenter(),
                point,
                lmi.Constant.Input.Mouse.LEFT,
                lmi.Constant.Input.Modifier.NONE);
    }

    // do action
    public static void dig() {
        act(lmi.Constant.Action.DIG);
    }

    public static void mine() {
        act(lmi.Constant.Action.MINE);
    }

    public static void carry() {
        act(lmi.Constant.Action.CARRY);
    }

    public static void destroy() {
        act(lmi.Constant.Action.DESTROY);
    }

    public static void fish() {
        act(lmi.Constant.Action.FISH);
    }

    public static void inspect() {
        act(lmi.Constant.Action.INSPECT);
    }

    public static void repair() {
        act(lmi.Constant.Action.REPAIR);
    }

    public static void crime() {
        act(lmi.Constant.Action.CRIME);
    }

    public static void swim() {
        act(lmi.Constant.Action.SWIM);
    }

    public static void tracking() {
        act(lmi.Constant.Action.TRACKING);
    }

    public static void aggro() {
        act(lmi.Constant.Action.AGGRO);
    }

    public static void shoot() {
        act(lmi.Constant.Action.SHOOT);
    }

    public static boolean act(String action) {
        return WidgetMessageHandler.act(lmi.ObjectShadow.gameUI().menu, action);
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

    // private methods
    private static boolean move_(haven.Coord2d point) {
        final haven.Coord pointInIntCoordinate = CoordinateHandler.convertCoord2dToCoord(point);
        moveDestination_ = point;
        return WidgetMessageHandler.click(
                lmi.ObjectShadow.mapView(),
                Util.mapViewCenter(),
                pointInIntCoordinate,
                lmi.Constant.Input.Mouse.LEFT,
                lmi.Constant.Input.Modifier.NONE);
    }

    private static boolean move_(haven.Coord point) {
        moveDestination_ = CoordinateHandler.convertCoordToCoord2d(point);
        return WidgetMessageHandler.click(
                lmi.ObjectShadow.mapView(),
                Util.mapViewCenter(),
                point,
                lmi.Constant.Input.Mouse.LEFT,
                lmi.Constant.Input.Modifier.NONE);
    }
}
