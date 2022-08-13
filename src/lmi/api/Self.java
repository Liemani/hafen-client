package lmi.api;

public class Self {
    // access properties
    public static haven.Gob gob() {
        return lmi.ObjectShadow.mapView_.player();
    }

    public static haven.Coord2d location() {
        return gob().rc;
    }

    public static haven.Skeleton.Pose pose() {
         return gob().getpose();
    }

    public static double hardHitPoint() {
        haven.IMeter gaugeWidget = getMeterWidgetByResourceName(lmi.Constant.Gauge.Index.HIT_POINT);
        return haven.LMI.gaugeMeters(gaugeWidget).get(lmi.Constant.Gauge.HitPointIndex.HARD).a;
    }

    public static double softHitPoint() {
        haven.IMeter gaugeWidget = getMeterWidgetByResourceName(lmi.Constant.Gauge.Index.HIT_POINT);
        return haven.LMI.gaugeMeters(gaugeWidget).get(lmi.Constant.Gauge.HitPointIndex.SOFT).a;
    }

    public static double stamina() {
        haven.IMeter gaugeWidget = getMeterWidgetByResourceName(lmi.Constant.Gauge.Index.STAMINA);
        return haven.LMI.gaugeMeters(gaugeWidget).get(0).a;
    }

    public static double energy() {
        haven.IMeter gaugeWidget = getMeterWidgetByResourceName(lmi.Constant.Gauge.Index.ENERGY);
        return haven.LMI.gaugeMeters(gaugeWidget).get(0).a;
    }

    public static haven.IMeter getMeterWidgetByResourceName(int gaugeIndex) {
        // lmi.Constant.Guage.Index
        return lmi.ObjectShadow.gaugeArray_[gaugeIndex];
    }

    public static double velocity() {
        return GobHandler.velocity(gob());
    }

    // simple move
    public static void moveNorthTile() {
        move(CoordinateHandler.northTile(location()));
    }

    // move
    public static void moveAndWaitArriving(haven.Coord2d clickedMapPoint) throws InterruptedException {
        move(clickedMapPoint);
        waitArriving();
    }

    public static void move(haven.Coord2d clickedMapPoint) {
        // 1 tile has 11.0 width
        WidgetMessageHandler.mapViewClick(
                lmi.ObjectShadow.mapView_,
                Util.mapViewCenter_,
                CoordinateHandler.convertCoord2dToCoord(clickedMapPoint),
                lmi.Constant.Input.Mouse.LEFT,
                lmi.Constant.Input.Modifier.NONE);
    }

    public static void moveByIntCoordinate(haven.Coord clickedMapPoint) {
        // 1 tile has 1024 width
        WidgetMessageHandler.mapViewClick(
                lmi.ObjectShadow.mapView_,
                Util.mapViewCenter_,
                clickedMapPoint,
                lmi.Constant.Input.Mouse.LEFT,
                lmi.Constant.Input.Modifier.NONE);
    }

    public static void waitArriving() throws InterruptedException {
        final long startTime = System.currentTimeMillis();
        final long timeoutLimit = startTime + lmi.Constant.Time.GENERAL_TIMEOUT;
        while (Self.velocity() == 0) {
            Thread.sleep(lmi.Constant.Time.GENERAL_SLEEP);
            long currentTime = System.currentTimeMillis();
            if (currentTime > timeoutLimit)
                break;
        }
        while (Self.velocity() != 0)
            Thread.sleep(lmi.Constant.Time.GENERAL_SLEEP);
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
        WidgetMessageHandler.act(lmi.ObjectShadow.gameUI_.menu, action);
    }
}
