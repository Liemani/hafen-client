package lmi.api;

import lmi.*;
import lmi.Constant.*;
import static lmi.Constant.*;
import static lmi.Constant.Input.Mouse.*;
import static lmi.Constant.Input.Modifier.*;
import static lmi.Constant.InteractionType.*;
import static lmi.Constant.MeshId.*;
import static lmi.Constant.Action.*;

public class WidgetMessageHandler {
    // public method
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    public static StatusCode actionClick(haven.Gob gob) {
        haven.Coord gobLocationInCoord = GobHandler.locationInCoord(gob);
        return WidgetMessageHandler.sendClickMessage(
                ObjectShadow.mapView(),
                Util.mapViewCenter(),
                gobLocationInCoord,
                IM_LEFT,
                IM_NONE,
                IT_DEFAULT,
                GobHandler.id(gob),
                gobLocationInCoord,
                0,
                MI_NONE);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    public static StatusCode interact(haven.Gob gob, int meshId) {
        haven.Coord gobLocationInCoord = GobHandler.locationInCoord(gob);
        return sendClickMessage(
                ObjectShadow.mapView(),
                Util.mapViewCenter(),
                gobLocationInCoord,
                IM_RIGHT,
                IM_NONE,
                IT_DEFAULT,
                GobHandler.id(gob),
                gobLocationInCoord,
                0,
                meshId);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    public static StatusCode put(haven.Coord location) {
        return sendClickMessage(
                ObjectShadow.mapView(),
                Util.mapViewCenter(),
                location,
                IM_RIGHT,
                IM_NONE);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    public static StatusCode selectCharacter(haven.Charlist widget, String name) {
        return sendSelectCharacterMessage(widget, name);
    }

    // package method
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    static StatusCode sendClickMessage(
            haven.MapView widget,
            haven.Coord clickedMapViewPoint,
            haven.Coord clickedMapPoint,
            int mouseButton,
            int modifiers) {
        return wdgmsg_(
                widget,
                A_CLICK,
                clickedMapViewPoint,
                clickedMapPoint,
                mouseButton,
                modifiers);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    static StatusCode sendClickMessage(
            haven.MapView widget,
            haven.Coord clickedMapViewPoint,
            haven.Coord clickedMapPoint,
            int mouseButton,
            int modifiers,
            int interactionType,
            int gobId,
            haven.Coord gobLocation,
            int overlayId,
            int meshId) {
        return wdgmsg_(
                widget,
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

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    static StatusCode sendActionMessage(haven.MenuGrid widget, String action) {
        return wdgmsg_(widget, A_ACT, action, 0);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    static StatusCode sendCancelActionMessage() {
        return sendClickMessage(
                ObjectShadow.mapView(),
                Util.mapViewCenter(),
                Self.locationInCoord(),
                IM_RIGHT,
                IM_NONE);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    static StatusCode sendChoosePetalMessage(haven.FlowerMenu widget, int index) {
        return wdgmsg_(
                widget,
                A_CLOSE_FLOWER_MENU,
                index,
                0);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    static StatusCode sendCloseFlowerMenuMessage(haven.FlowerMenu widget) {
        return wdgmsg_(
                widget,
                A_CLOSE_FLOWER_MENU,
                -1);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    static StatusCode sendSelectCharacterMessage(haven.Charlist widget, String name) {
        return wdgmsg_(widget, A_SELECT_CHARACTER, name);
    }

    // private method
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode wdgmsg_(haven.Widget widget, String action, Object... args) {
        widget.wdgmsg(action, args);
        return WaitManager.waitAction(action);
    }

    // deprecated method
    @Deprecated
    public static StatusCode openFlowerMenuByClickData(haven.MapView widget, haven.ClickData clickData) {
        final haven.Gob gob = Util.gobFromClickData(clickData);
        Object[] args = {
            Util.mapViewCenter(),
            CoordinateHandler.convertCoord2dToCoord(gob.rc),
            IM_RIGHT,
            IM_NONE};
        if(clickData != null)
            args = haven.Utils.extend(args, clickData.clickargs());
        return wdgmsg_(widget, A_CLICK, args);
    }
}
