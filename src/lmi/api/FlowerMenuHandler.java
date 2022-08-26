package lmi.api;

import lmi.*;

public class FlowerMenuHandler {
    // assume widget_ never collision
    private static haven.FlowerMenu widget_;
    private static boolean isWidgetOpened_ = false;

    public static haven.FlowerMenu widget() {
        synchronized(FlowerMenuHandler.class) {
            return widget_;
        }
    }

    @Deprecated
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
    private static final Object openMonitor_ = new Object();
    // TODO refine implementation
    public static boolean open(haven.Gob gob, int meshId) {
        WidgetMessageHandler.interact(gob, meshId);
        Util.addTimer(openMonitor_);

        try {
            synchronized (openMonitor_) {
                openMonitor_.wait();
            }
        } catch (InterruptedException e) {
            Util.removeTimer();
            return false;
        }

        Util.removeTimer();
        return true;
    }

    public static void notifyOpen() {
        synchronized (openMonitor_) {
            openMonitor_.notify();
        }
    }

    // choose
    // TODO 한 번 더 시도하고 없어야 없다고 할 수 있다
    // TODO 지금 open을 사용하는데 이거 안쓰는게 좋을 것 같다
    public static void chooseByGobAndPetalName(haven.Gob gob, String name) {
        boolean result = FlowerMenuHandler.open(gob, Constant.MeshId.DEFAULT);
        try {
            if (result == false)
                return;    // flower menu didn't open -> there is nothing can interact
            FlowerMenuHandler.chooseByName("Take branch");
        } catch (IllegalArgumentException e) {
            boolean isClosed = FlowerMenuHandler.close();
            if (!isClosed)
                System.out.println("flower menu failed closing");
            return;    // no that name petal -> there is no to work
        }
    }

    public static void chooseByName(String name) throws IllegalArgumentException {
        if (name == null)
            throw new IllegalArgumentException();

        for (haven.FlowerMenu.Petal petal : FlowerMenuHandler.widget().opts)
            if (petal.name.contentEquals(name)) {
                try {
                    chooseByIndex(petal.num);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                return;
            }

        throw new IllegalArgumentException();
    }

    // private methods
    private static void interact(haven.Gob gob, int meshId) {
        // If you don't know meshID, pass -1
        WidgetMessageHandler.interact(gob, meshId);
    }

    private static void chooseByIndex(int index) throws IndexOutOfBoundsException {
        WidgetMessageHandler.choosePetalByIndex(FlowerMenuHandler.widget(), index);
    }

    // close
    private static final Object closeMonitor_ = new Object();
    public static boolean close() {
        WidgetMessageHandler.closeFlowerMenu(widget());
        Util.addTimer(closeMonitor_);

        try {
            synchronized (closeMonitor_) {
                closeMonitor_.wait();
            }
        } catch (InterruptedException e) {
            Util.removeTimer();
            return false;
        }

        Util.removeTimer();
        return true;
    }

    public static void notifyClose() {
        synchronized (closeMonitor_) {
            closeMonitor_.notify();
        }
    }
}
