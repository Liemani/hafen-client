package lmi.macro;

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
        if (lmi.ObjectShadow.mapView_ == null)
            return null;
        else
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

    // do action
    public static void dig() {
        doAct(lmi.Constant.Command.Action.DIG);
    }

    public static void mine() {
        doAct(lmi.Constant.Command.Action.MINE);
    }

    public static void carry() {
        doAct(lmi.Constant.Command.Action.CARRY);
    }

    public static void destroy() {
        doAct(lmi.Constant.Command.Action.DESTROY);
    }

    public static void fish() {
        doAct(lmi.Constant.Command.Action.FISH);
    }

    public static void inspect() {
        doAct(lmi.Constant.Command.Action.INSPECT);
    }

    public static void repair() {
        doAct(lmi.Constant.Command.Action.REPAIR);
    }

    public static void crime() {
        doAct(lmi.Constant.Command.Action.CRIME);
    }

    public static void swim() {
        doAct(lmi.Constant.Command.Action.SWIM);
    }

    public static void tracking() {
        doAct(lmi.Constant.Command.Action.TRACKING);
    }

    public static void aggro() {
        doAct(lmi.Constant.Command.Action.AGGRO);
    }

    public static void shoot() {
        doAct(lmi.Constant.Command.Action.SHOOT);
    }

    public static void doAct(String action) {
        lmi.ObjectShadow.gameUI_.menu.wdgmsg(lmi.Constant.Command.ACTION, action);
    }

    // mapClick()
    public static void mapClickLeftMouseButton(haven.Coord2d mapPoint, int modifiers) {
        mapClick(mapPoint, lmi.Constant.Mouse.Button.LEFT, modifiers);
    }

    public static void mapClickRightMouseButton(haven.Coord2d mapPoint, int modifiers) {
        mapClick(mapPoint, lmi.Constant.Mouse.Button.RIGHT, modifiers);
    }

    public static void mapClick(haven.Coord2d mapPoint, int button, int modifiers) {
        lmi.ObjectShadow.mapView_.wdgmsg(
                lmi.Constant.Command.CLICK,
                mapViewCenter(),
                Util.convertCoord2dToCoord(mapPoint),
                button,
                modifiers);
    }

    public static void mapClickInCoord(haven.Coord mapPoint, int button, int modifiers) {
        lmi.ObjectShadow.mapView_.wdgmsg(
                lmi.Constant.Command.CLICK,
                mapViewCenter(),
                mapPoint,
                button,
                modifiers);
    }

    private static haven.Coord mapViewCenter() {
		return lmi.ObjectShadow.mapView_.sz.div(2);
	}
}
