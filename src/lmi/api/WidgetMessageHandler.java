package lmi.api;

import lmi.*;

public class WidgetMessageHandler {
    // public method
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    public static Constant.StatusCode interact(haven.Gob gob, int meshId) {
        haven.Coord gobLocationInCoord = GobHandler.locationInCoord(gob);
        return sendObjectClickMessage(
                ObjectShadow.mapView(),
                Util.mapViewCenter(),
                gobLocationInCoord,
                Constant.Input.Mouse.RIGHT,
                Constant.Input.Modifier.NONE,
                Constant.InteractionType.DEFAULT,
                GobHandler.id(gob),
                gobLocationInCoord,
                0,
                meshId);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    public static Constant.StatusCode put(haven.Coord location) {
        return sendClickMessage(
                ObjectShadow.mapView(),
                Util.mapViewCenter(),
                location,
                Constant.Input.Mouse.RIGHT,
                Constant.Input.Modifier.NONE);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    public static Constant.StatusCode selectCharacter(haven.Charlist widget, String name) {
        return sendSelectCharacterMessage(widget, name);
    }

    // package method
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    static Constant.StatusCode sendClickMessage(
            haven.MapView widget,
            haven.Coord clickedMapViewPoint,
            haven.Coord clickedMapPoint,
            int mouseButton,
            int modifiers) {
        return wdgmsg_(
                widget,
                lmi.Constant.Command.CLICK,
                clickedMapViewPoint,
                clickedMapPoint,
                mouseButton,
                modifiers);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    static Constant.StatusCode sendObjectClickMessage(
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
                lmi.Constant.Command.CLICK,
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
    static Constant.StatusCode sendActMessage(haven.MenuGrid widget, String action) {
        return wdgmsg_(widget, lmi.Constant.Command.ACT, action, 0);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    static Constant.StatusCode sendCancelActMessage() {
        return sendClickMessage(
                ObjectShadow.mapView(),
                Util.mapViewCenter(),
                Self.locationInCoord(),
                Constant.Input.Mouse.RIGHT,
                Constant.Input.Modifier.NONE);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    static Constant.StatusCode sendChoosePetalMessage(haven.FlowerMenu widget, int index) {
        return wdgmsg_(
                widget,
                lmi.Constant.Command.CLOSE_FLOWER_MENU,
                index,
                0);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    static Constant.StatusCode sendCloseFlowerMenuMessage(haven.FlowerMenu widget) {
        return wdgmsg_(
                widget,
                lmi.Constant.Command.CLOSE_FLOWER_MENU,
                -1);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    static Constant.StatusCode sendSelectCharacterMessage(haven.Charlist widget, String name) {
        return wdgmsg_(widget, lmi.Constant.Command.SELECT_CHARACTER, name);
    }

    // private method
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static Constant.StatusCode wdgmsg_(haven.Widget widget, String command, Object... args) {
        widget.wdgmsg(command, args);
        return WaitManager.waitCommand(command);
    }

    // deprecated method
    @Deprecated
    public static Constant.StatusCode openFlowerMenuByClickData(haven.MapView widget, haven.ClickData clickData) {
        final haven.Gob gob = Util.gobFromClickData(clickData);
        Object[] args = {
            Util.mapViewCenter(),
            CoordinateHandler.convertCoord2dToCoord(gob.rc),
            Constant.Input.Mouse.RIGHT,
            Constant.Input.Modifier.NONE};
        if(clickData != null)
            args = haven.Utils.extend(args, clickData.clickargs());
        return wdgmsg_(widget, lmi.Constant.Command.CLICK, args);
    }
}
