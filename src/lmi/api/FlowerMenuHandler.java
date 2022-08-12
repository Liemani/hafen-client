package lmi.api;

public class FlowerMenuHandler {
    static haven.FlowerMenu flowerMenu_;

    public static void set(haven.FlowerMenu flowerMenu) {
        flowerMenu_ = flowerMenu;
    }

    // If you don't know meshID, pass -1
    public static void open(haven.Gob gob, int meshId) {
        haven.Coord gobLocationInCoord = CoordinateHandler.convertCoord2dToCoord(gob.rc);
        WidgetMessageHandler.openFlowerMenu(
                lmi.ObjectShadow.mapView_,
                Util.mapViewCenter_,
                gobLocationInCoord,
                lmi.Constant.InteractionType.GENERAL,
                (int)gob.id,
                gobLocationInCoord,
                0,
                meshId);
    }

    // TODO 플라워 메뉴를 스테이틱 변수에 직접 설정함에 따라 widget()이 반환하는 값을 이 값으로 수정하고
    // 이미 구현되어 있는 widget() 함수는 추후 사용할지도 모르니 구체적인 다른 이름으로 남겨두자
    public static haven.FlowerMenu widget() {
        haven.Widget lastChildOfRootWidget = lmi.ObjectShadow.rootWidget_.lchild;
        if (lastChildOfRootWidget instanceof haven.FlowerMenu)
            return (haven.FlowerMenu)lmi.ObjectShadow.rootWidget_.lchild;
        else
            return null;
    }

    // wait open: if opened, return true, else false
    public static boolean waitOpen() throws InterruptedException {
        final long startTime = System.currentTimeMillis();
        final long timeoutLimit = startTime + lmi.Constant.Time.GENERAL_TIMEOUT;
        while (widget() == null) {
            Thread.sleep(lmi.Constant.Time.GENERAL_SLEEP);
            long currentTime = System.currentTimeMillis();
            if (currentTime > timeoutLimit)
                return false;
        }
        return true;
    }
    // choose
    // TODO 한 번 더 시도하고 없어야 없다고 할 수 있다.
    public static void chooseByGobAndPetalName(haven.Gob gob, String name) {
        try {
            boolean result = waitOpen();
            if (result == false)
                return;    // flower menu didn't open -> there is nothing can interact
            chooseAndWaitByName("Take branch");
        } catch (IllegalArgumentException e) {
            cancel();
            return;    // no that name petal -> there is no to work
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void chooseAndWaitByName(String name) throws IllegalArgumentException, InterruptedException {
        chooseByName(name);
        Util.waitArriving();
        Util.waitHourGlassFailable();
    }

    public static void chooseByName(String name) throws IllegalArgumentException {
        for (haven.FlowerMenu.Petal petal : widget().opts)
            if (petal.name.contentEquals(name)) {
                try {
                    chooseByIndex(petal.num);
                } catch (IndexOutOfBoundsException e) { e.printStackTrace(); }
                return;
            }
        throw new IllegalArgumentException();
    }

    public static void chooseByIndex(int index) throws IndexOutOfBoundsException {
        WidgetMessageHandler.choosePetalByIndex(widget(), index);
    }

    public static void cancel() {
        WidgetMessageHandler.cancelFlowerMenu(widget());
    }
}
