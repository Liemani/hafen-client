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
    private static final Object movingMonitor_ = new Object();
    public static boolean moveAndWaitArriving(haven.Coord2d mapPoint) throws InterruptedException {
        move(mapPoint);
        return waitArriving(mapPoint);
    }

    public static void waitMoveStrict(haven.Coord2d mapPoint) throws InterruptedException {
        move(mapPoint);
        synchronized (movingMonitor_) {
            movingMonitor_.wait();
        }
        moveDestination_ = null;
    }

    // OCache::$move::apply()
    // LinMove::$linstep::apply()
    public static void notifyIfArrived(haven.Gob gob) {
        if (!Self.coordinateEquals(moveDestination_))
            return;

        synchronized (movingMonitor_) {
            movingMonitor_.notify();
        }
    }

    public static void move(haven.Coord2d mapPoint) {
        final haven.Coord mapPointInIntCoordinate = CoordinateHandler.convertCoord2dToCoord(mapPoint);
        moveDestination_ = mapPoint;
        WidgetMessageHandler.click(
                lmi.ObjectShadow.mapView(),
                Util.mapViewCenter(),
                mapPointInIntCoordinate,
                lmi.Constant.Input.Mouse.LEFT,
                lmi.Constant.Input.Modifier.NONE);
    }

    public static void moveByIntCoordinate(haven.Coord mapPoint) {
        // 1 tile has 1024 width
        WidgetMessageHandler.click(
                lmi.ObjectShadow.mapView(),
                Util.mapViewCenter(),
                mapPoint,
                lmi.Constant.Input.Mouse.LEFT,
                lmi.Constant.Input.Modifier.NONE);
    }

    public static boolean waitArriving(haven.Coord2d destination) throws InterruptedException {
        long currentTime = System.currentTimeMillis();
        long timeoutLimit = currentTime + lmi.Constant.Time.GENERAL_TIMEOUT;
        while (!Self.isArrived(destination)) {
            currentTime = System.currentTimeMillis();
            if (currentTime > timeoutLimit
                    && Self.velocity() == 0.0)
                return false;
            if (Self.velocity() != 0.0)
                timeoutLimit = currentTime + lmi.Constant.Time.GENERAL_TIMEOUT;
            Thread.sleep(lmi.Constant.Time.GENERAL_SLEEP);
        }
        return true;
    }

    public static boolean isArrived(haven.Coord2d destination) {
        return Self.coordinateEquals(destination);
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

    public static void act(String action) {
        WidgetMessageHandler.act(lmi.ObjectShadow.gameUI().menu, action);
    }

    // etc
    public static boolean moveNorthTile() throws InterruptedException {
        haven.Coord2d northTile = CoordinateHandler.northTile(Self.location());
        final boolean result = moveAndWaitArriving(northTile);
        return result;
    }

    public static void waitMoveNorthTileStrict() throws InterruptedException {
        haven.Coord2d northTile = CoordinateHandler.northTile(Self.location());
        waitMoveStrict(northTile);
    }

    public static double distance(haven.Gob gob) {
        return Self.location().dist(GobHandler.location(gob));
    }

    public static boolean moveCenter() throws InterruptedException {
        haven.Coord2d center = CoordinateHandler.tileCenter(Self.location());
        final boolean result = moveAndWaitArriving(center);
        return result;
    }

    public static void waitMoveCenterStrict() throws InterruptedException {
        haven.Coord2d center = CoordinateHandler.tileCenter(Self.location());
        waitMoveStrict(center);
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
}
