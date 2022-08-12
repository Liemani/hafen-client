package lmi.api;

public class Interaction {
    // TODO interact
    public static void interact(haven.Coord2d mapPoint, int modifiers) {
        WidgetMessageHandler.mapViewClick(
                lmi.ObjectShadow.mapView_,
                Util.mapViewCenter_,
                CoordinateHandler.convertCoord2dToCoord(mapPoint),
                lmi.Constant.Input.Mouse.RIGHT,
                modifiers);
    }
}
