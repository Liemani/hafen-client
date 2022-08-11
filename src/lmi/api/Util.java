package lmi.api;

//  좌표 체계는 double 값으로 11의 배수가 각 타일의 한 꼭지점이다
//  int로는 한 타일은 1024의 간격을 갖는다

public class Util {
    static haven.ClickData clickData_;

    public static void storeClickedData(haven.ClickData clickData) {
        clickData_ = clickData;
    }

    public static haven.ClickData clickData() { return clickData_; }

    public static haven.Gob clickedGob() {
        return gobByClickData(clickData());
    }

    public static haven.Gob gobByClickData(haven.ClickData clickData) {
        if (clickData == null)
            return null;

        final haven.Clickable clickable = clickData.ci;
        if (clickable.getClass() != haven.Gob.GobClick.class)
            return null;

        final haven.Gob.GobClick gobClick = (haven.Gob.GobClick)clickable;
        return gobClick.gob;
    }

    public static void describeClickedGob() { lmi.Debug.debugDescribeField(clickedGob()); }

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

    public static void selectCharacter(String name) {
        lmi.ObjectShadow.characterList_.wdgmsg(lmi.Constant.Command.SELECT_CHARACTER, name);
    }

    public static haven.Coord convertCoord2dToCoord(haven.Coord2d point) {
        return new haven.Coord(point.floor(haven.OCache.posres));
    }
}
