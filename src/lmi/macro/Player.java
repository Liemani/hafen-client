package lmi.macro;

public class Player {
    public static void debugDescribe() {
        System.out.println("[gob()]");
        lmi.Debug.debugDescribeField(gob());
        System.out.println("[location()]");
        lmi.Debug.debugDescribeField(location());
        System.out.println("[getPose()]");
        lmi.Debug.debugDescribeField(getPose());
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

    public static haven.Skeleton.Pose getPose() {
         return gob().getpose();
    }

    // meter widget data
    public static double hardHitPoint() {
        haven.IMeter meterWidget = getMeterWidgetByResourceName(lmi.Constant.Meter.ResourceName.HIT_POINT);
        return meterWidget.meters.get(lmi.Constant.Meter.HitPointIndex.HARD).a;
    }

    public static double softHitPoint() {
        haven.IMeter meterWidget = getMeterWidgetByResourceName(lmi.Constant.Meter.ResourceName.HIT_POINT);
        return meterWidget.meters.get(lmi.Constant.Meter.HitPointIndex.SOFT).a;
    }

    public static double stamina() {
        haven.IMeter meterWidget = getMeterWidgetByResourceName(lmi.Constant.Meter.ResourceName.STAMINA);
        return meterWidget.meters.get(0).a;
    }

    public static double energy() {
        haven.IMeter meterWidget = getMeterWidgetByResourceName(lmi.Constant.Meter.ResourceName.ENERGY);
        return meterWidget.meters.get(0).a;
    }

    public static haven.IMeter getMeterWidgetByResourceName(String name) {
        java.util.List<haven.Widget> meterList = lmi.ObjectShadow.gameUI_.meters;
        for (haven.Widget widget : meterList) {
            if (widget instanceof haven.IMeter) {
                haven.IMeter meterWidget = (haven.IMeter)widget;
                if (meterWidget.bg.get().name.contentEquals(name))
                    return meterWidget;
            }
        }
        return null;
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
                mapPoint.floor(haven.OCache.posres),
                button,
                modifiers);
    }

    private static haven.Coord mapViewCenter() {
		return lmi.ObjectShadow.mapView_.sz.div(2);
	}
}
