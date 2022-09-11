package lmi;

// import haven
import haven.Gob;
import haven.Coord;

// import constant
import static lmi.Constant.*;
import static lmi.Constant.Input.Mouse.*;
import static lmi.Constant.Input.Modifier.*;
import static lmi.Constant.InteractionType.*;
import static lmi.Constant.MeshId.*;
import static lmi.Constant.Action.*;

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
                A_CLICK,
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
                A_CLICK,
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
        widget.sendMessage(A_ACT, action, 0);
    }

    static void sendChoosePetalMessage(haven.FlowerMenu widget, int index) {
        widget.sendMessage(A_CLOSE_FLOWER_MENU, index, 0);
    }

    static void sendCloseFlowerMenuMessage(haven.FlowerMenu widget) {
        widget.sendMessage(A_CLOSE_FLOWER_MENU, -1);
    }

    static void sendSelectCharacterMessage(haven.Charlist widget, String name) {
        widget.sendMessage(A_SELECT_CHARACTER, name);
    }

    static void sendMenuGridBarrelMessage() {
        WidgetManager.menuGrid().sendMessage(A_ACT, "bp", "barrel", 0);
    }
}
