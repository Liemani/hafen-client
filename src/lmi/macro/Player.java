package lmi.macro;

class Player {
    static void debugDescribe() {
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
    static haven.Gob gob() {
        if (lmi.ObjectShadow.mapView_ == null)
            return null;
        else
            return lmi.ObjectShadow.mapView_.player();
    }

    // methods
    static haven.Coord2d location() {
        return gob().rc;
    }

    static haven.Skeleton.Pose getPose() {
         return gob().getpose();
    }

    // meter widget data
    static double hardHitPoint() {
        haven.IMeter meterWidget = getMeterWidgetByName("gfx/hud/meter/hp");
        return meterWidget.meters.get(0).a;
    }

    static double softHitPoint() {
        haven.IMeter meterWidget = getMeterWidgetByName("gfx/hud/meter/hp");
        return meterWidget.meters.get(1).a;
    }

    static double stamina() {
        haven.IMeter meterWidget = getMeterWidgetByName("gfx/hud/meter/stam");
        return meterWidget.meters.get(0).a;
    }

    static double energy() {
        haven.IMeter meterWidget = getMeterWidgetByName("gfx/hud/meter/nrj");
        return meterWidget.meters.get(0).a;
    }

    static haven.IMeter getMeterWidgetByName(String name) {
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
    static void dig() {
        doAct("dig");
    }

    static void mine() {
        doAct("mine");
    }

    static void carry() {
        doAct("carry");
    }

    static void destroy() {
        doAct("destroy");
    }

    static void fish() {
        doAct("fish");
    }

    static void inspect() {
        doAct("inspect");
    }

    static void repair() {
        doAct("repair");
    }

    static void crime() {
        doAct("crime");
    }

    static void swim() {
        doAct("swim");
    }

    static void tracking() {
        doAct("tracking");
    }

    static void aggro() {
        doAct("aggro");
    }

    static void shoot() {
        doAct("shoot");
    }

    static void doAct(String action) {
        lmi.ObjectShadow.gameUI_.menu.wdgmsg("act", action);
    }

    // TODO move another file! I want make this to package access!
    static void mapViewClick(double x, double y, int btn, int mod) {
        lmi.ObjectShadow.mapView_.wdgmsg("click", getCenterScreenCoord(), new haven.Coord2d(x, y).floor(haven.OCache.posres), btn, mod);
    }

    private static haven.Coord getCenterScreenCoord() {
		return lmi.ObjectShadow.mapView_.sz.div(2);
	}

//  	    reference: Object[] args = {pc, mc.floor(posres), clickb, ui.modflags()};
//      private static void mapViewClick2(double x, double y, int btn, int mod) {
//          lmi.ObjectShadow.mapView_.wdgmsg("click", getCenterScreenCoord(), new haven.Coord2d(x, y).floor(haven.OCache.posres), btn, mod);
//      }
}
