package lmi.api;

import lmi.*;
import lmi.Constant.*;
import static lmi.Constant.Command.*;
import static lmi.Constant.Input.Mouse.*;
import static lmi.Constant.Input.Modifier.*;
import static lmi.Constant.InteractionType.*;

public class WidgetMessageHandler {
    // public method
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    public static StatusCode interact(haven.Gob gob, int meshId) {
        haven.Coord gobLocationInCoord = GobHandler.locationInCoord(gob);
        return sendObjectClickMessage(
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
                Command.C_CLICK,
                clickedMapViewPoint,
                clickedMapPoint,
                mouseButton,
                modifiers);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    static StatusCode sendObjectClickMessage(
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
                Command.C_CLICK,
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
    static StatusCode sendActMessage(haven.MenuGrid widget, String action) {
        return wdgmsg_(widget, C_ACT, action, 0);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    static StatusCode sendCancelActMessage() {
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
                Command.C_CLOSE_FLOWER_MENU,
                index,
                0);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    static StatusCode sendCloseFlowerMenuMessage(haven.FlowerMenu widget) {
        return wdgmsg_(
                widget,
                Command.C_CLOSE_FLOWER_MENU,
                -1);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    static StatusCode sendSelectCharacterMessage(haven.Charlist widget, String name) {
        return wdgmsg_(widget, Command.C_SELECT_CHARACTER, name);
    }

    // private method
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode wdgmsg_(haven.Widget widget, String command, Object... args) {
        widget.wdgmsg(command, args);
        return WaitManager.waitCommand(command);
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
        return wdgmsg_(widget, Command.C_CLICK, args);
    }
}
