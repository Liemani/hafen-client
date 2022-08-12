package lmi.api;

public class FlowerMenuHandler {
    // assume widget_ never collision
    // 따라서 일단 열렸다 닫히면 반드시 의도한 flower menu가 열렸던 것으로 간주한다
    private static haven.FlowerMenu widget_;
    private static boolean isWidgetOpened_ = false;

    public static haven.FlowerMenu widget() {
        synchronized(widget_) {
            return widget_;
        }
    }

    public static haven.FlowerMenu widgetAnotherWay() {
        haven.Widget lastChildOfRootWidget = lmi.ObjectShadow.rootWidget_.lchild;
        if (lastChildOfRootWidget instanceof haven.FlowerMenu)
            return (haven.FlowerMenu)lmi.ObjectShadow.rootWidget_.lchild;
        else
            return null;
    }

    public static void setWidget(haven.FlowerMenu widget) {
        synchronized(widget_) {
            widget_ = widget;
            isWidgetOpened_ = true;
        }
    }

    // open
    public static void openWait(haven.Gob gob, int meshId) throws InterruptedException {
        open(gob, meshId);
        waitOpen();
    }

    public static void open(haven.Gob gob, int meshId) {
        // If you don't know meshID, pass -1
        haven.Coord gobLocationInCoord = CoordinateHandler.convertCoord2dToCoord(gob.rc);
        isWidgetOpened_ = false;
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

    // TODO return state or thorow exception?
    // wait open: if opened, return true, else false
    public static boolean waitOpen() throws InterruptedException {
        final long startTime = System.currentTimeMillis();
        final long timeoutLimit = startTime + lmi.Constant.Time.GENERAL_TIMEOUT;
        while (!isWidgetOpened_) {
            Thread.sleep(lmi.Constant.Time.GENERAL_SLEEP);
            final long currentTime = System.currentTimeMillis();
            if (currentTime > timeoutLimit)
                return false;
        }
        return true;
    }

    public static boolean waitOpenAnotherWay() throws InterruptedException {
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

    // cancel
    public static void cancel() {
        WidgetMessageHandler.cancelFlowerMenu(widget());
    }
}
