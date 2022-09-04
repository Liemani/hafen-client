package lmi.api;

import haven.Gob;
import haven.Coord;

import lmi.*;
import static lmi.Constant.TimeOut.*;

//  좌표 체계는 double 값으로 11의 배수가 각 타일의 한 꼭지점이다
//  int로는 한 타일은 1024의 간격을 갖는다

public class Util {
    // TODO 화면 사이즈가 바뀌면 이 값도 바꿔주도록 하자
    // 아마 frame에 sizeChanged() 같은 event가 있을 것 같다
    private static Coord mapViewCenter_;

    public static Coord mapViewCenter() {
        return Util.mapViewCenter_;
    }

    public static void initMapViewCenterByMapView(haven.MapView mapView) {
        mapViewCenter_ = mapView.sz.div(2);
    }

    // TODO re-implement
    public static void waitHourGlassFailable() throws InterruptedException {
        final long startTime = System.currentTimeMillis();
        final long timeoutLimit = startTime + TO_GENERAL;
        while (lmi.ObjectShadow.gameUI().prog == null) {
            Thread.sleep(TO_TEMPORARY);
            long currentTime = System.currentTimeMillis();
            if (currentTime > timeoutLimit)
                break;
        }
        while (lmi.ObjectShadow.gameUI().prog != null)
            Thread.sleep(TO_TEMPORARY);
    }

    public static void newWidget(haven.Widget widget) {
        if (widget.getClass() == haven.FlowerMenu.class) {
            FlowerMenuHandler.setWidget((haven.FlowerMenu)widget);
        }
    }

//      iter = oc.iterator();
//      while(iter.hasNext()) {
//          System.out.println(iter.next());
//      }
    public static java.util.Iterator<Gob> iterator() { return ObjectShadow.objectCache().iterator(); }

    // find gob
    public static Gob closestGob() {
        java.util.Iterator<Gob> iterator;
        Gob gob = null;
        while (true) {
            try {
                iterator = Util.iterator();
                gob = closestGob(iterator);
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return gob;
    }

    private static Gob closestGob(java.util.Iterator<Gob> iterator) {
        Gob gob;
        double distance;

        Gob closestGob = null;
        double closestDistance = 1100.0;

        while (iterator.hasNext()) {
            gob = iterator.next();
            if (gob.getClass() != Gob.class)
                continue;
            if (Self.gob().isAt(gob.location()))
                continue;
            distance = Self.gob().distance(gob);
            if (distance < closestDistance) {
                closestGob = gob;
                closestDistance = distance;
            }
        }

        return closestGob;
    }
}
