package lmi.api;

public class Self {
    public static void debugDescribe() {
        System.out.println("[gob()]");
        lmi.Debug.debugDescribeField(gob());
        System.out.println("[location()]");
        lmi.Debug.debugDescribeField(location());
        System.out.println("[pose()]");
        lmi.Debug.debugDescribeField(pose());
        System.out.println("[hardHitPoint()]");
        lmi.Debug.debugDescribeField(hardHitPoint());
        System.out.println("[softHitPoint()]");
        lmi.Debug.debugDescribeField(softHitPoint());
        System.out.println("[stamina()]");
        lmi.Debug.debugDescribeField(stamina());
        System.out.println("[energy()]");
        lmi.Debug.debugDescribeField(energy());
    }

    // never reuse return value(it could be changed)
    public static haven.Gob gob() {
        return lmi.ObjectShadow.mapView_.player();
    }

    // methods
    public static haven.Coord2d location() {
        return gob().rc;
    }

    public static haven.Skeleton.Pose pose() {
         return gob().getpose();
    }

    // gauge data
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
        return lmi.ObjectShadow.gaugeArray_[gaugeIndex];
    }

    public static double velocity() {
        return GobHandler.velocity(gob());
    }

    public static void moveNorthTile() {
        Interaction.mapClickLeftMouseButton(CoordinateHandler.north1Tile(location()), 0);
    }
}
