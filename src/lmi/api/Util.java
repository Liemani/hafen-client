package lmi.api;

//  좌표 체계는 double 값으로 11의 배수가 각 타일의 한 꼭지점이다
//  int로는 한 타일은 1024의 간격을 갖는다

public class Util {
    // TODO 화면 사이즈가 바뀌면 이 값도 바꿔주도록 하자
    // 아마 frame에 sizeChanged() 같은 event가 있을 것 같다
    static haven.Coord mapViewCenter_;
    static haven.ClickData clickData_;

    public static void initMapViewCenterByMapView(haven.MapView mapView) {
        mapViewCenter_ = mapView.sz.div(2);
    }

    public static void storeClickedData(haven.ClickData clickData) {
        clickData_ = clickData;
    }

    public static haven.ClickData clickData() { return clickData_; }

    public static haven.Gob clickedGob() {
        return gobFromClickData(clickData());
    }

    public static haven.Gob gobFromClickData(haven.ClickData clickData) {
        if (clickData == null)
            return null;

        final haven.Clickable clickable = clickData.ci;
        if (clickable.getClass() != haven.Gob.GobClick.class)
            return null;

        final haven.Gob.GobClick gobClick = (haven.Gob.GobClick)clickable;
        return gobClick.gob;
    }

    public static void describeClickedGob() { lmi.Debug.describeField(clickedGob()); }

    public static void waitHourGlassFailable() throws InterruptedException {
        final long startTime = System.currentTimeMillis();
        final long timeoutLimit = startTime + lmi.Constant.Time.GENERAL_TIMEOUT;
        while (lmi.ObjectShadow.gameUI_.prog == null) {
            Thread.sleep(lmi.Constant.Time.GENERAL_SLEEP);
            long currentTime = System.currentTimeMillis();
            if (currentTime > timeoutLimit)
                break;
        }
        while (lmi.ObjectShadow.gameUI_.prog != null)
            Thread.sleep(lmi.Constant.Time.GENERAL_SLEEP);
    }

    public static void newWidget(haven.Widget widget) {
        if (widget.getClass() == haven.FlowerMenu.class) {
            FlowerMenuHandler.setWidget((haven.FlowerMenu)widget);
        }
    }
}
