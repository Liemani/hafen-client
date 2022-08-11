package lmi.api;

public class FlowerMenuHandler {
    public static haven.FlowerMenu widget() {
        haven.Widget lastChildOfRootWidget = lmi.ObjectShadow.rootWidget_.lchild;
        if (lastChildOfRootWidget instanceof haven.FlowerMenu)
            return (haven.FlowerMenu)lmi.ObjectShadow.rootWidget_.lchild;
        else
            return null;
    }

    // open
    public static void openByGob(haven.Gob gob) {
        final haven.Coord gobLocation = Util.convertCoord2dToCoord(lmi.api.Util.clickedGob().rc);
        sendWidgetMessageForFlowerMenuOpen(
                Interaction.mapViewCenter(),
                gobLocation,
                0,  // i don't know '(int)1' case
                (int)lmi.api.Util.clickedGob().id,
                gobLocation,
                0,  // i don't know using overlay case
                -1);    // i don't know where this is used
    }

    public static void sendWidgetMessageForFlowerMenuOpen(haven.Coord mapViewClickPoint, haven.Coord mapClickPoint, int hasOverlay, int gobId, haven.Coord gobLocationInCoord, int overlayId, int meshId) {
        lmi.ObjectShadow.mapView_.wdgmsg(
                lmi.Constant.Command.CLICK,
                mapViewClickPoint,
                mapClickPoint,
                lmi.Constant.Mouse.Button.RIGHT,
                0,
                hasOverlay,
                gobId,
                gobLocationInCoord,
                overlayId,
                meshId);
    }

    public static void openByClickData(haven.ClickData clickData) {
        final haven.Gob gob = Util.gobByClickData(clickData);
        Object[] args = {
            Interaction.mapViewCenter(),
            Util.convertCoord2dToCoord(gob.rc),
            lmi.Constant.Mouse.Button.RIGHT,
            0};
        if(clickData != null)
            args = haven.Utils.extend(args, clickData.clickargs());
        lmi.ObjectShadow.mapView_.wdgmsg(lmi.Constant.Command.CLICK, args);
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

    // cancel
    public static void cancel() {
        widget().wdgmsg(lmi.Constant.Command.FLOWER_MENU, -1);
    }

    // choose
    // TODO 한 번 더 시도하고 없어야 없다고 할 수 있다.
    public static void chooseByGobAndPetalName(haven.Gob gob, String name) {
        try {
            boolean result = lmi.api.FlowerMenuHandler.waitOpen();
            if (result == false)
                return;    // flower menu didn't open -> there is nothing can interact
            lmi.api.FlowerMenuHandler.chooseAndWaitByName("Take branch");
        } catch (IllegalArgumentException e) {
            lmi.api.FlowerMenuHandler.cancel();
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
                chooseByIndex(petal.num);
                return;
            }
        throw new IllegalArgumentException();
    }

    public static void chooseByIndex(int index) throws IndexOutOfBoundsException {
        if (0 <= index && index < widget().opts.length)
            widget().wdgmsg(lmi.Constant.Command.FLOWER_MENU, index, 0);
        else
            throw new IndexOutOfBoundsException();
    }
}
