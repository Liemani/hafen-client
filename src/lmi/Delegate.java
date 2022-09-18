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
    public static void newWidgetDidAdded(Widget widget) {
        if (widget.getClass() == FlowerMenu.class) {
            FlowerMenuHandler.setWidget((FlowerMenu)widget);
            WaitManager.notifySignal(S_FLOWER_MENU_DID_ADDED);
        } else if (widget.getClass() == Window.class) {
            final Window window = (Window)widget;
//              if (window.isTitle(text))
//                  lmi.delegate.windowDidAdded(window);
        }
    }

    // flowerMenu
    public static void flowerMenuDidDestroyed() {
        FlowerMenuHandler.clearWidget();
    }

    // linMove
    public static void linMoveDidAdded(Gob gob) {
        WaitManager.notifySignal(S_MOVE_DID_BEGIN, gob);
    }

    public static void linMoveDidDeleted(Gob gob) {
        WaitManager.notifySignal(S_MOVE_DID_END, gob);
    }

    // following
    public static void followingDidAdded(Gob gob) {
        final Gob target = gob.followingTarget();

        if (target == Self.gob())
            WaitManager.notifySignal(S_DID_LIFT, target);
    }

    public static void followingDidDeleted(Gob gob) {
        final Gob target = gob.followingTarget();
        if (target != Self.gob()) return;

        WaitManager.notifySignal(S_DID_PUT, target);
    }

    // progress
    public static void progressDidAdded(GameUI.Progress widget) {
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
            WaitManager.notifySignal(S_DID_PUT, gob);
    }

    public static boolean didClicked(Coord2d coord2d, int mouseButton, ClickData clickData) {
        if (!WaitManager.isWaitingSignal(S_OBJECT_DID_CLICKED))
            return false;

        if (mouseButton == IM_LEFT && clickData != null) {
            ClickManager.setClickData(clickData);
            WaitManager.notifySignal(S_OBJECT_DID_CLICKED);
            return true;
        }

        return false;
    }

    public static void didGetACK(RMessage rMessage) {
        final String message = MessageHandler.getAction(rMessage);
        Util.debugPrint("rMessage: \"" + message + "\"");
        WaitManager.notifyMessage(message);
    }

    public static boolean keyDidDown(java.awt.event.KeyEvent keyEvent) {
        if (AutomationManager.isRunning() && AWTEventGenerator.isESC(keyEvent)) {
            AutomationManager.interrupt();
            return true;
        }
        return false;
    }

    public static boolean areaDidSelected(Coord first, Coord second) {
        if (WaitManager.isWaitingSignal(S_AREA_DID_SELECTED)) {
            final Rect selectedArea = new Rect(first, second);
            selectedArea.origin.assignMultiply(TILE_IN_COORD);
            selectedArea.size.assignAdd(1)
                .assignMultiply(TILE_IN_COORD);
            ClickManager.setSelectedArea(selectedArea);
            WaitManager.notifySignal(S_AREA_DID_SELECTED);
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
//              WaitManager.notifySignal(S_PLOB_DID_PLACED);
    }
}
