package lmi.api;

import lmi.*;

// TODO change all methods to get sender as argument
public class WidgetMessageHandler {
    // act
    public static boolean sendActMessage(haven.MenuGrid widget, String action) {
        return wdgmsg_(widget, lmi.Constant.Command.ACT, action);
    }

    // TODO implement behavior or this could locate on Self if didn't use wdgmsg_()
    public static void sendCancelActMessage() {
    }

    // flower menu
    public static boolean sendChoosePetalMessage(haven.FlowerMenu widget, int index) {
        return wdgmsg_(
                widget,
                lmi.Constant.Command.FLOWER_MENU_CLOSE,
                index,
                0);
    }

    public static boolean sendCloseFlowerMenuMessage(haven.FlowerMenu widget) {
        return wdgmsg_(
                widget,
                lmi.Constant.Command.FLOWER_MENU_CLOSE,
                -1);
    }

    // character list
    public static boolean sendSelectCharacterMessage(haven.Charlist widget, String name) {
        return wdgmsg_(widget, lmi.Constant.Command.SELECT_CHARACTER, name);
    }

    // click
    public static boolean move(haven.Coord2d point) {
        return WidgetMessageHandler.move(CoordinateHandler.convertCoord2dToCoord(point));
    }

    public static boolean move(haven.Coord point) {
        return sendClickMessage_(
                ObjectShadow.mapView(),
                Util.mapViewCenter(),
                point,
                lmi.Constant.Input.Mouse.LEFT,
                lmi.Constant.Input.Modifier.NONE);
    }

    public static boolean interact(haven.Gob gob, int meshId) {
        haven.Coord gobLocationInCoord = GobHandler.locationInCoord(gob);
        return sendObjectClickMessage_(
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

    public static boolean actClick(haven.Gob gob) {
        haven.Coord gobLocationInCoord = GobHandler.locationInCoord(gob);
        return sendObjectClickMessage_(
                ObjectShadow.mapView(),
                Util.mapViewCenter(),
                gobLocationInCoord,
                Constant.Input.Mouse.LEFT,
                Constant.Input.Modifier.NONE,
                Constant.InteractionType.DEFAULT,
                GobHandler.id(gob),
                gobLocationInCoord,
                0,
                Constant.MeshId.NONE);
    }

    public static boolean put(haven.Coord location) {
        return sendClickMessage_(
            ObjectShadow.mapView(),
            Util.mapViewCenter(),
            location,
            Constant.Input.Mouse.RIGHT,
            Constant.Input.Modifier.NONE);
    }

    // private
    private static boolean wdgmsg_(haven.Widget widget, String command, Object... args) {
        widget.wdgmsg(command, args);
        return WaitManager.wait(command);
    }

    private static boolean sendClickMessage_(
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

    private static boolean sendObjectClickMessage_(
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
