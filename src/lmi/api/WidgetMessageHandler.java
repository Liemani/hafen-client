package lmi.api;

import lmi.*;

// TODO change all methods to get sender as argument
public class WidgetMessageHandler {
    // act
    public static void act(haven.MenuGrid menuGrid, String action) {
        // look lmi.Constant.Action for available String value
        menuGrid.wdgmsg(lmi.Constant.Command.ACT, action);
    }

    // TODO implement behavior or this could locate on lmi.api.Self if didn't use wdgmsg()
    public static void cancelAct() {
    }

    // click
    public static void click(
            haven.MapView mapView,
            haven.Coord clickedMapViewPoint,
            haven.Coord clickedMapPoint,
            int mouseButton,
            int modifiers) {
        mapView.wdgmsg(
                lmi.Constant.Command.CLICK,
                clickedMapViewPoint,
                clickedMapPoint,
                mouseButton,
                modifiers);
    }

    public static void clickObject(
            haven.MapView mapView,
            haven.Coord clickedMapViewPoint,
            haven.Coord clickedMapPoint,
            int mouseButton,
            int modifiers,
            int interactionType,
            int gobId,
            haven.Coord gobLocation,
            int overlayId,
            int meshId) {
        mapView.wdgmsg(
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

    public static void openFlowerMenuByClickData(haven.MapView mapView, haven.ClickData clickData) {
        final haven.Gob gob = Util.gobFromClickData(clickData);
        Object[] args = {
            Util.mapViewCenter(),
            CoordinateHandler.convertCoord2dToCoord(gob.rc),
            lmi.Constant.Input.Mouse.RIGHT,
            0};
        if(clickData != null)
            args = haven.Utils.extend(args, clickData.clickargs());
        mapView.wdgmsg(lmi.Constant.Command.CLICK, args);
    }

    public static void choosePetalByIndex(haven.FlowerMenu flowerMenu, int index) throws IndexOutOfBoundsException {
        if (0 <= index && index < flowerMenu.opts.length)
            flowerMenu.wdgmsg(lmi.Constant.Command.FLOWER_MENU, index, 0);
        else
            throw new IndexOutOfBoundsException();
    }

    public static void cancelFlowerMenu(haven.FlowerMenu flowerMenu) {
        flowerMenu.wdgmsg(lmi.Constant.Command.FLOWER_MENU, -1);
    }

    // characterList
    public static void selectCharacter(haven.Charlist charList, String name) {
        charList.wdgmsg(lmi.Constant.Command.SELECT_CHARACTER, name);
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
