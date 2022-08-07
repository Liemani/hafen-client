package lmi;

class Player {
    static void debugDescribe() {
        System.out.println("[gob()]");
        Debug.debugDescribeField(gob());
        System.out.println("[location()]");
        Debug.debugDescribeField(location());
        System.out.println("[getPose()]");
        Debug.debugDescribeField(getPose());
        System.out.println("[hardHitPoint()]");
        Debug.debugDescribeField(hardHitPoint());
        System.out.println("[softHitPoint()]");
        Debug.debugDescribeField(softHitPoint());
        System.out.println("[stamina()]");
        Debug.debugDescribeField(stamina());
        System.out.println("[energy()]");
        Debug.debugDescribeField(energy());
    }

    // never reuse return value(it could be changed)
    static haven.Gob gob() {
        if (ObjectShadow.mapView_ == null)
            return null;
        else
            return ObjectShadow.mapView_.player();
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
        java.util.List<haven.Widget> meterList = ObjectShadow.gameUI_.meters;
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
        ObjectShadow.gameUI_.menu.wdgmsg("act", action);
    }
}
