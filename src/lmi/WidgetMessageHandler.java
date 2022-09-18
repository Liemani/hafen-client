package lmi;

// import haven
import haven.Gob;
import haven.Coord;

// import constant
import static lmi.Constant.*;
import static lmi.Constant.Message.*;
import static lmi.Constant.Action.*;
import static lmi.Constant.Plob.*;
import static lmi.Constant.Input.Mouse.*;
import static lmi.Constant.Input.Modifier.*;
import static lmi.Constant.InteractionType.*;
import static lmi.Constant.MeshId.*;

class WidgetMessageHandler {
    static void click(Coord coord, int mouseButton, int modifier) {
        WidgetMessageHandler.sendClickMessage(
                ObjectShadow.mapView(),
                Util.mapViewCenter(),
                coord,
                mouseButton,
                modifier);
    }

    static void cancelAction() {
        WidgetMessageHandler.click(Self.location(), IM_RIGHT, IM_NONE);
    }

    static void click(Gob gob, int mouseButton, int modifier, int meshId) {
        final Coord gobLocation = gob.location();
        WidgetMessageHandler.sendClickMessage(
                ObjectShadow.mapView(),
                Util.mapViewCenter(),
                gobLocation,
                mouseButton,
                modifier,
                IT_DEFAULT,
                gob.id(),
                gobLocation,
                0,
                meshId);
    }

    static void click(Gob gob, int mouseButton, int modifier) {
        WidgetMessageHandler.click(gob, mouseButton, modifier, MI_NONE);
    }

    static void selectCharacter(haven.Charlist widget, String name) {
        WidgetMessageHandler.sendSelectCharacterMessage(widget, name);
    }

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

    static void sendChoosePetalMessage(haven.FlowerMenu widget, int index) {
        widget.sendMessage(M_CL, index, 0);
    }

    static void sendCloseFlowerMenuMessage(haven.FlowerMenu widget) {
        widget.sendMessage(M_CL, -1);
    }

    static void sendSelectCharacterMessage(haven.Charlist widget, String name) {
        widget.sendMessage(M_PLAY, name);
    }

    static void sendPlaceMessage(
            haven.MapView widget,
            Coord coord,
            int direction,
            int mouseButton,
            int modifiers) {
        widget.wdgmsg(M_PLACE, coord, direction, mouseButton, modifiers);
    }

    static void sendBuildPlacingMessage(String name) {
        WidgetManager.menuGrid().sendMessage(M_ACT, A_BP, name, 0);
    }
}
