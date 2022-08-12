package lmi.api;

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

    // MapView click
    public static void mapViewClick(
            haven.MapView mapView,
            haven.Coord clickedMapViewPoint,
            haven.Coord clickedMapPoint,
            int button,
            int modifiers) {
        mapView.wdgmsg(
                lmi.Constant.Command.CLICK,
                clickedMapViewPoint,
                clickedMapPoint,
                button,
                modifiers);
    }

    // FlowerMenu
    public static void openFlowerMenu(
            haven.MapView mapView,
            haven.Coord clickedMapViewPoint,
            haven.Coord clickedMapPoint,
            int interactionType,
            int gobId,
            haven.Coord gobLocation,
            int overlayId,
            int meshId) {
        mapView.wdgmsg(
                lmi.Constant.Command.CLICK,
                clickedMapViewPoint,
                clickedMapPoint,
                lmi.Constant.Input.Mouse.RIGHT,
                0,
                interactionType,
                gobId,
                gobLocation,
                overlayId,
                meshId);
    }

    public static void openFlowerMenuByClickData(haven.MapView mapView, haven.ClickData clickData) {
        final haven.Gob gob = Util.gobFromClickData(clickData);
        Object[] args = {
            Util.mapViewCenter_,
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
}
