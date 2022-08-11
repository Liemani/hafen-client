package lmi.api;

public class Interaction {
    // do action
    public static void dig() {
        doAct(lmi.Constant.Command.Action.DIG);
    }

    public static void mine() {
        doAct(lmi.Constant.Command.Action.MINE);
    }

    public static void carry() {
        doAct(lmi.Constant.Command.Action.CARRY);
    }

    public static void destroy() {
        doAct(lmi.Constant.Command.Action.DESTROY);
    }

    public static void fish() {
        doAct(lmi.Constant.Command.Action.FISH);
    }

    public static void inspect() {
        doAct(lmi.Constant.Command.Action.INSPECT);
    }

    public static void repair() {
        doAct(lmi.Constant.Command.Action.REPAIR);
    }

    public static void crime() {
        doAct(lmi.Constant.Command.Action.CRIME);
    }

    public static void swim() {
        doAct(lmi.Constant.Command.Action.SWIM);
    }

    public static void tracking() {
        doAct(lmi.Constant.Command.Action.TRACKING);
    }

    public static void aggro() {
        doAct(lmi.Constant.Command.Action.AGGRO);
    }

    public static void shoot() {
        doAct(lmi.Constant.Command.Action.SHOOT);
    }

    public static void doAct(String action) {
        lmi.ObjectShadow.gameUI_.menu.wdgmsg(lmi.Constant.Command.ACTION, action);
    }

    // mapClick()
    public static void mapClickLeftMouseButton(haven.Coord2d mapPoint, int modifiers) {
        mapClick(mapPoint, lmi.Constant.Mouse.Button.LEFT, modifiers);
    }

    public static void mapClickRightMouseButton(haven.Coord2d mapPoint, int modifiers) {
        mapClick(mapPoint, lmi.Constant.Mouse.Button.RIGHT, modifiers);
    }

    public static void mapClick(haven.Coord2d mapPoint, int button, int modifiers) {
        lmi.ObjectShadow.mapView_.wdgmsg(
                lmi.Constant.Command.CLICK,
                mapViewCenter(),
                Util.convertCoord2dToCoord(mapPoint),
                button,
                modifiers);
    }

    public static void mapClickInCoord(haven.Coord mapPoint, int button, int modifiers) {
        lmi.ObjectShadow.mapView_.wdgmsg(
                lmi.Constant.Command.CLICK,
                mapViewCenter(),
                mapPoint,
                button,
                modifiers);
    }

    public static haven.Coord mapViewCenter() {
		return lmi.ObjectShadow.mapView_.sz.div(2);
	}
}
