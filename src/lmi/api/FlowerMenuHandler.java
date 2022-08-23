package lmi.api;

public class FlowerMenuHandler {
    // assume widget_ never collision
    private static haven.FlowerMenu widget_;
    private static boolean isWidgetOpened_ = false;

    public static haven.FlowerMenu widget() {
        synchronized(FlowerMenuHandler.class) {
            return widget_;
        }
    }

    public static haven.FlowerMenu widgetAnotherWay() {
        haven.Widget lastChildOfRootWidget = lmi.ObjectShadow.rootWidget().lchild;
        if (lastChildOfRootWidget instanceof haven.FlowerMenu)
            return (haven.FlowerMenu)lmi.ObjectShadow.rootWidget().lchild;
        else
            return null;
    }

    public static void setWidget(haven.FlowerMenu widget) {
        synchronized(FlowerMenuHandler.class) {
            widget_ = widget;
            isWidgetOpened_ = true;
        }
    }

    // open
    public static void interactWait(haven.Gob gob, int meshId) throws InterruptedException {
        FlowerMenuHandler.interact(gob, meshId);
        waitOpeningSimple();
    }

    public static void interact(haven.Gob gob, int meshId) {
        // If you don't know meshID, pass -1
        isWidgetOpened_ = false;
        WidgetMessageHandler.interact(gob, meshId);
    }

    // TODO implement this
    public static boolean waitOpeningStrict() throws InterruptedException {
        return false;
    }

    // wait opening: if opened, return true, else false
    public static boolean waitOpeningSimple() throws InterruptedException {
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
    // TODO 한 번 더 시도하고 없어야 없다고 할 수 있다
    // TODO 지금 waitOpeningSimple을 사용하는데 이거 안쓰는게 좋을 것 같다
    public static void chooseByGobAndPetalName(haven.Gob gob, String name) {
        try {
            boolean result = waitOpeningSimple();
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
