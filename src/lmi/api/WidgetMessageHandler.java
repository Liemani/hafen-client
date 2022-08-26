package lmi.api;

import lmi.*;

// TODO change all methods to get sender as argument
public class WidgetMessageHandler {
    // act
    public static void act(haven.MenuGrid widget, String action) {
        // look lmi.Constant.Action for available String value
        WidgetMessageHandler.wdgmsg(widget, lmi.Constant.Command.ACT);
    }

    // TODO implement behavior or this could locate on lmi.api.Self if didn't use wdgmsg()
    public static void cancelAct() {
    }

    // click
    public static void click(
            haven.MapView widget,
            haven.Coord clickedMapViewPoint,
            haven.Coord clickedMapPoint,
            int mouseButton,
            int modifiers) {
        WidgetMessageHandler.wdgmsg(
                widget,
                lmi.Constant.Command.CLICK,
                clickedMapViewPoint,
                clickedMapPoint,
                mouseButton,
                modifiers);
    }

    public static void clickObject(
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
        WidgetMessageHandler.wdgmsg(
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

    public static void wdgmsg(haven.Widget widget, String command, Object... args) {
        widget.wdgmsg(command, args);
        WaitManager.waitMessage(command);
    }

    @Deprecated
    public static void openFlowerMenuByClickData(haven.MapView widget, haven.ClickData clickData) {
        final haven.Gob gob = Util.gobFromClickData(clickData);
        Object[] args = {
            Util.mapViewCenter(),
            CoordinateHandler.convertCoord2dToCoord(gob.rc),
            lmi.Constant.Input.Mouse.RIGHT,
            0};
        if(clickData != null)
            args = haven.Utils.extend(args, clickData.clickargs());
        WidgetMessageHandler.wdgmsg(widget, lmi.Constant.Command.CLICK, args);
    }

    public static void choosePetalByIndex(haven.FlowerMenu widget, int index) throws IndexOutOfBoundsException {
        if (widget == null)
            return;

        final int petalCount = widget.opts.length;
        if (0 <= index && index < petalCount) {
            WidgetMessageHandler.wdgmsg(
                    widget,
                    lmi.Constant.Command.FLOWER_MENU_CLOSE,
                    index,
                    0);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public static void closeFlowerMenu(haven.FlowerMenu widget) {
        WidgetMessageHandler.wdgmsg(
                widget,
                lmi.Constant.Command.FLOWER_MENU_CLOSE,
                -1);
    }

    // characterList
    public static void selectCharacter(haven.Charlist widget, String name) {
        WidgetMessageHandler.wdgmsg(widget, lmi.Constant.Command.SELECT_CHARACTER, name);
    }

    // convenient
    public static void interact(haven.Gob gob, int meshId) {
        haven.Coord2d gobLocation = GobHandler.location(gob);
        haven.Coord gobLocationInCoord = CoordinateHandler.convertCoord2dToCoord(gobLocation);
        WidgetMessageHandler.clickObject(
                ObjectShadow.mapView(),
                lmi.api.Util.mapViewCenter(),
                gobLocationInCoord,
                Constant.Input.Mouse.RIGHT,
                Constant.Input.Modifier.NONE,
                Constant.InteractionType.DEFAULT,
                GobHandler.id(gob),
                gobLocationInCoord,
                0,
                meshId);
    }

    public static void lift(haven.Gob gob) {
        Self.carry();
        haven.Coord2d gobLocation = GobHandler.location(gob);
        haven.Coord gobLocationInCoord = CoordinateHandler.convertCoord2dToCoord(gobLocation);
        WidgetMessageHandler.clickObject(
                ObjectShadow.mapView(),
                lmi.api.Util.mapViewCenter(),
                gobLocationInCoord,
                Constant.Input.Mouse.LEFT,
                Constant.Input.Modifier.NONE,
                Constant.InteractionType.DEFAULT,
                GobHandler.id(gob),
                gobLocationInCoord,
                0,
                Constant.MeshId.NONE);
    }

    public static void put(haven.Coord location) {
        click(
            ObjectShadow.mapView(),
            lmi.api.Util.mapViewCenter(),
            location,
            Constant.Input.Mouse.RIGHT,
            Constant.Input.Modifier.NONE);
    }
}
