package lmi;

// import java.util
import java.util.ArrayList;

// import haven
import haven.*;

// constant
import static lmi.Constant.*;
import static lmi.Constant.Message.*;
import static lmi.Constant.Signal.*;
import static lmi.Constant.Input.Mouse.*;

// resource
import static lmi.Constant.gfx.borka.*;

public class Delegate {
    // widget
    public static void newWidgetDidAdded(haven.Widget widget) {
        if (widget.getClass() == haven.FlowerMenu.class)
            WaitManager.notifySignal(S_FLOWER_MENU_DID_ADDED);
        lmi.Debug.describeClassNameHashCodeWithTag("widget: ", widget);
    }

    // flowerMenu
    public static void flowerMenuDidCreated(haven.FlowerMenu widget) {
        FlowerMenuHandler.setWidget(widget);
    }

    public static void flowerMenuDidDestroyed() {
        FlowerMenuHandler.clearWidget();
    }

    // linMove
    public static void linMoveDidAdded(Gob gob) {
        WaitManager.notifySignal(gob, S_MOVE_DID_BEGIN);
    }

    public static void linMoveDidDeleted(Gob gob) {
        WaitManager.notifySignal(gob, S_MOVE_DID_END);
    }

    // following
    public static void followingDidAdded(Gob gob) {
        final Gob target = gob.followingTarget();
        if (target != Self.gob()) return;

        WaitManager.notifySignal(target, S_DID_LIFT);
    }

    public static void followingDidDeleted(Gob gob) {
        final Gob target = gob.followingTarget();
        if (target != Self.gob()) return;

        WaitManager.notifySignal(target, S_DID_PUT);
    }

    // progress
    public static void progressDidAdded(haven.GameUI.Progress widget) {
        ProgressManager.setWidget(widget);
        WaitManager.notifySignal(S_PROGRESS_DID_ADDED);
    }

    public static void progressDidDestroyed() {
        ProgressManager.setWidget(null);
        WaitManager.notifySignal(S_PROGRESS_DID_DESTROYED);
    }

    // etc
    public static void poseDidChanged(Gob gob) {
        if (gob.hasPose(RN_IDLE))
            WaitManager.notifySignal(gob, S_DID_PUT);
    }

    public static boolean didClicked(haven.Coord2d coord2d, int mouseButton, haven.ClickData clickData) {
        if (!WaitManager.isWaitingSignal(S_DID_OBJECT_CLICK))
            return false;

        if (mouseButton == IM_LEFT && clickData != null) {
            ClickManager.setClickData(clickData);
            WaitManager.notifySignal(S_DID_OBJECT_CLICK);
            return true;
        }

        return false;
    }

    public static void cursorDidChanged() {
        WaitManager.notifyMessage(M_CURS);
    }

    public static void didGetACK(haven.RMessage rMessage) {
        final String message = MessageHandler.getAction(rMessage);
        Util.debugPrint("rMessage: \"" + message + "\"");
        WaitManager.notifyMessage(message);
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
        if (WaitManager.isWaitingSignal(S_DID_AREA_SELECTED)) {
            final Rect selectedArea = new Rect(first, second);
            selectedArea.origin.assignMultiply(TILE_IN_COORD);
            selectedArea.size.assignAdd(1)
                .assignMultiply(TILE_IN_COORD);
            ClickManager.setSelectedArea(selectedArea);
            WaitManager.notifySignal(S_DID_AREA_SELECTED);
            return true;
        } else {
            return false;
        }
    }

    // Did Constructed
    public static void remoteUIDidConstructed(RemoteUI remoteUI) {
        Initializer.initRemoteUI(remoteUI);
    }

    // etc
    public static void plobDidPlaced(MapView.Plob plob) {
//          if (!isWaitingDidPlobPlacedSignal()) return;
//  
//          if (plob.resourceNameEndsWith(""/*what i want*/))
//              WaitManager.notifySignal(S_DID_PLOB_PLACED);
    }
}
