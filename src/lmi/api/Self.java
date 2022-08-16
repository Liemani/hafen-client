package lmi.api;

public class Self {
    // access properties
    public static haven.Gob gob() {
        return lmi.ObjectShadow.mapView().player();
    }

    public static haven.Coord2d location() {
        return gob().rc;
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
    public static boolean moveAndWaitArriving(haven.Coord2d mapPoint) throws InterruptedException {
        move(mapPoint);
        return waitArriving(mapPoint);
    }

    public static void move(haven.Coord2d clickedMapPoint) {
        // 1 tile has 11.0 width
        WidgetMessageHandler.mapViewClick(
                lmi.ObjectShadow.mapView(),
                Util.mapViewCenter_,
                CoordinateHandler.convertCoord2dToCoord(clickedMapPoint),
                lmi.Constant.Input.Mouse.LEFT,
                lmi.Constant.Input.Modifier.NONE);
    }

    public static void moveByIntCoordinate(haven.Coord clickedMapPoint) {
        // 1 tile has 1024 width
        WidgetMessageHandler.mapViewClick(
                lmi.ObjectShadow.mapView(),
                Util.mapViewCenter_,
                clickedMapPoint,
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
        return Math.abs(Self.location().x - destination.x) < lmi.Constant.COORD2D_PER_COORD;
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
    // simple move
    public static void moveNorthTile() {
        move(CoordinateHandler.northTile(location()));
    }
}
