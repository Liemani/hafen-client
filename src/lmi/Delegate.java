package lmi;

// import java.util
import java.util.ArrayList;

// import haven
import haven.*;

// constant
import static lmi.Constant.*;
import static lmi.Constant.Action.*;
import static lmi.Constant.Action.Custom.*;
import static lmi.Constant.Input.Mouse.*;

// resource
import static lmi.Constant.gfx.borka.*;

public class Delegate {
    // widget
    public static void newWidgetDidAdded(haven.Widget widget) {
        if (widget.getClass() == haven.FlowerMenu.class)
            WaitManager.notifyAction(AC_FLOWER_MENU_DID_ADDED);
        lmi.Debug.describeClassNameHashCodeWithTag("widget: ", widget);
    }

    // flowerMenu
    public static void flowerMenuDidCreated(haven.FlowerMenu widget) {
        FlowerMenuHandler.setWidget(widget);
    }

    public static void flowerMenuDidCanceled() {
        WaitManager.notifyAction(A_CLOSE_FLOWER_MENU);
    }

    public static void flowerMenuDidChosen() {
        WaitManager.notifyAction(A_CLOSE_FLOWER_MENU);
    }

    public static void flowerMenuDidDestroyed() {
        FlowerMenuHandler.clearWidget();
    }

    // linMove
    public static void linMoveDidAdded(Gob gob) {
        WaitManager.notifyAction(gob, AC_MOVE_DID_BEGIN);
    }

    public static void linMoveDidDeleted(Gob gob) {
        WaitManager.notifyAction(gob, AC_MOVE_DID_END);
    }

    // following
    public static void followingDidAdded(Gob gob) {
        final Gob target = gob.followingTarget();
        if (target != Self.gob()) return;

        WaitManager.notifyAction(target, AC_DID_LIFT);
    }

    public static void followingDidDeleted(Gob gob) {
        final Gob target = gob.followingTarget();
        if (target != Self.gob()) return;

        WaitManager.notifyAction(target, AC_DID_PUT);
    }

    // progress
    public static void progressDidAdded(haven.GameUI.Progress widget) {
        ProgressManager.setWidget(widget);
        WaitManager.notifyAction(AC_PROGRESS_DID_ADDED);
    }

    public static void progressDidDestroyed() {
        ProgressManager.setWidget(null);
        WaitManager.notifyAction(AC_PROGRESS_DID_DESTROYED);
    }

    // etc
    public static void poseDidChanged(Gob gob) {
        if (gob.hasPose(RN_IDLE))
            WaitManager.notifyAction(gob, AC_DID_PUT);
    }

    public static boolean didClicked(haven.Coord2d coord2d, int mouseButton, haven.ClickData clickData) {
        if (ClickManager.isWaitingMouseDown() && mouseButton == IM_LEFT) {
            if (clickData != null) {
                ClickManager.setClickData(clickData);
                WaitManager.notifyAction(AC_DID_OBJECT_CLICK);
            }

            ClickManager.setClickPoint(Coord.of(coord2d));
            WaitManager.notifyAction(AC_DID_CLICK);
            return true;
        }
        return false;
    }

    public static void cursorDidChanged() {
        WaitManager.notifyAction(A_CHANGE_CURSOR);
    }

    public static void didGetACK(haven.RMessage message) {
        final String command = MessageHandler.getAction(message);
        Util.debugPrint("command: " + command);
        WaitManager.notifyAction(command);
    }

    public static void gobArrayCopyed(ArrayList<Gob> array) {
        GobManager.setGobArray(array);
    }

    public static boolean keyDidDown(java.awt.event.KeyEvent keyEvent) {
        if (AutomationManager.isRunning() && AWTEventGenerator.isESC(keyEvent)) {
            AutomationManager.interrupt();
            return true;
        }
        return false;
    }

    public static boolean areaDidSelect(Coord first, Coord second) {
        if (ClickManager.isWaitingMouseUp()) {
            final Rect selectedArea = new Rect(first, second);
            selectedArea.origin.assignMultiply(TILE_IN_COORD);
            selectedArea.size.assignAdd(1)
                .assignMultiply(TILE_IN_COORD);
            ClickManager.setSelectedArea(selectedArea);
            WaitManager.notifyAction(AC_DID_AREA_SELECTED);
            return true;
        } else {
            return false;
        }
    }

    // Did Constructed
    public static void remoteUIDidConstructed(RemoteUI remoteUI) {
        Initializer.initRemoteUI(remoteUI);
    }
}
