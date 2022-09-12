package lmi;

// import haven
import haven.Gob;
import haven.Coord;

// import constant
import static lmi.Constant.*;
import static lmi.Constant.Message.*;
import static lmi.Constant.Action.*;
import static lmi.Constant.ActionTarget.*;
import static lmi.Constant.Input.Mouse.*;
import static lmi.Constant.Input.Modifier.*;
import static lmi.Constant.InteractionType.*;
import static lmi.Constant.MeshId.*;

public class WidgetMessageHandler {
    // public method
    public static void actionClick(Gob gob) {
        Coord gobLocationInCoord = gob.location();
        WidgetMessageHandler.sendClickMessage(
                ObjectShadow.mapView(),
                Util.mapViewCenter(),
                gobLocationInCoord,
                IM_LEFT,
                IM_NONE,
                IT_DEFAULT,
                gob.id(),
                gobLocationInCoord,
                0,
                MI_NONE);
    }

    public static void interact(Gob gob, int meshId) {
        Coord gobLocationInCoord = gob.location();
        WidgetMessageHandler.sendClickMessage(
                ObjectShadow.mapView(),
                Util.mapViewCenter(),
                gobLocationInCoord,
                IM_RIGHT,
                IM_NONE,
                IT_DEFAULT,
                gob.id(),
                gobLocationInCoord,
                0,
                meshId);
    }

    public static void put(Coord location) {
        WidgetMessageHandler.sendClickMessage(
                ObjectShadow.mapView(),
                Util.mapViewCenter(),
                location,
                IM_RIGHT,
                IM_NONE);
    }

    public static void selectCharacter(haven.Charlist widget, String name) {
        WidgetMessageHandler.sendSelectCharacterMessage(widget, name);
    }

    static void sendCancelActionMessage() {
        WidgetMessageHandler.sendClickMessage(
                ObjectShadow.mapView(),
                Util.mapViewCenter(),
                Self.location(),
                IM_RIGHT,
                IM_NONE);
    }

    // package method
    static void sendClickMessage(
            haven.MapView widget,
            Coord clickedMapViewPoint,
            Coord clickedMapPoint,
            int mouseButton,
            int modifiers) {
        widget.sendMessage(
                M_CLICK,
                clickedMapViewPoint,
                clickedMapPoint,
                mouseButton,
                modifiers);
    }

    static void sendClickMessage(
            haven.MapView widget,
            Coord clickedMapViewPoint,
            Coord clickedMapPoint,
            int mouseButton,
            int modifiers,
            int interactionType,
            int gobId,
            Coord gobLocation,
            int overlayId,
            int meshId) {
        widget.sendMessage(
                M_CLICK,
                clickedMapViewPoint,
                clickedMapPoint,
                mouseButton,
                modifiers,
                interactionType,
                gobId,
                gobLocation,
                overlayId,
                meshId);
    }

    static void sendActionMessage(haven.MenuGrid widget, String action) {
        widget.sendMessage(M_ACT, action, 0);
    }

    static void sendChoosePetalMessage(haven.FlowerMenu widget, int index) {
        widget.sendMessage(M_CL, index, 0);
    }

    static void sendCloseFlowerMenuMessage(haven.FlowerMenu widget) {
        widget.sendMessage(M_CL, -1);
    }

    static void sendSelectCharacterMessage(haven.Charlist widget, String name) {
        widget.sendMessage(M_PLAY, name);
    }

    static void sendMenuGridDryingFrameMessage() {
        WidgetManager.menuGrid().sendMessage(M_ACT, A_BP, AT_DFRAME, 0);
    }
}
