package lmi.api;

import lmi.*;

public class WidgetMessageHandler {
    // public methods
    public static boolean interact(haven.Gob gob, int meshId) {
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

    public static boolean put(haven.Coord location) {
        return sendClickMessage(
                ObjectShadow.mapView(),
                Util.mapViewCenter(),
                location,
                Constant.Input.Mouse.RIGHT,
                Constant.Input.Modifier.NONE);
    }

    public static boolean selectCharacter(haven.Charlist widget, String name) {
        return sendSelectCharacterMessage(widget, name);
    }

    // package methods
     static boolean sendClickMessage(
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

     static boolean sendObjectClickMessage(
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

     static boolean sendActMessage(haven.MenuGrid widget, String action) {
        return wdgmsg_(widget, lmi.Constant.Command.ACT, action, 0);
    }

    static boolean sendCancelActMessage() {
        return sendClickMessage(
                ObjectShadow.mapView(),
                Util.mapViewCenter(),
                Self.locationInCoord(),
                Constant.Input.Mouse.RIGHT,
                Constant.Input.Modifier.NONE);
    }

     static boolean sendChoosePetalMessage(haven.FlowerMenu widget, int index) {
        return wdgmsg_(
                widget,
                lmi.Constant.Command.CLOSE_FLOWER_MENU,
                index,
                0);
    }

     static boolean sendCloseFlowerMenuMessage(haven.FlowerMenu widget) {
        return wdgmsg_(
                widget,
                lmi.Constant.Command.CLOSE_FLOWER_MENU,
                -1);
    }

     static boolean sendSelectCharacterMessage(haven.Charlist widget, String name) {
        return wdgmsg_(widget, lmi.Constant.Command.SELECT_CHARACTER, name);
    }

    // private methods
    private static boolean wdgmsg_(haven.Widget widget, String command, Object... args) {
        widget.wdgmsg(command, args);
        return WaitManager.waitCommand(command);
    }

    @Deprecated
    public static boolean openFlowerMenuByClickData(haven.MapView widget, haven.ClickData clickData) {
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
