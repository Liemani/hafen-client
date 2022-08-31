package lmi.api;

import lmi.*;

// constant
import lmi.Constant.*;
import lmi.Constant.StatusCode;
import lmi.Constant.Command;
import static lmi.Constant.StatusCode.*;
import static lmi.Constant.Command.Custom.*;
import static lmi.Constant.Action.*;
import static lmi.Constant.Input.Mouse.*;
import static lmi.Constant.Input.Modifier.*;
import static lmi.Constant.InteractionType.*;
import static lmi.Constant.MeshId.*;
import static lmi.Constant.TimeOut.*;

public class Self {
    // access properties
    public static haven.Gob gob() {
        if (ObjectShadow.mapView() == null)
            return null;

        return ObjectShadow.mapView().player();
    }

    public static haven.Coord2d location() { return gob().rc; }
    public static double direction() { return gob().a; }
    public static double velocity() { return GobHandler.velocity(gob()); }
    public static haven.Skeleton.Pose pose() { return gob().getpose(); }

    public static haven.Coord locationInCoord() {
        return CoordinateHandler.convertCoord2dToCoord(Self.location());
    }

    public static double hardHitPoint() {
        return haven.LMI.gaugeWidgetGaugeArray(ObjectShadow.gaugeWidgetArray()[Gauge.Index.HIT_POINT])
            .get(Gauge.HitPointIndex.HARD)
            .a;
    }

    public static double softHitPoint() {
        return haven.LMI.gaugeWidgetGaugeArray(ObjectShadow.gaugeWidgetArray()[Gauge.Index.HIT_POINT])
            .get(Gauge.HitPointIndex.SOFT)
            .a;
    }

    public static double stamina() {
        return haven.LMI.gaugeWidgetGaugeArray(ObjectShadow.gaugeWidgetArray()[Gauge.Index.STAMINA])
            .get(0)
            .a;
    }

    public static double energy() {
        return haven.LMI.gaugeWidgetGaugeArray(ObjectShadow.gaugeWidgetArray()[Gauge.Index.ENERGY])
            .get(0)
            .a;
    }

    // move
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED_MOVE
    public static StatusCode move(haven.Coord2d point) {
        if (sendClickMessage_(point) == SC_INTERRUPTED) return SC_INTERRUPTED;
        return new MoveManager(Self.gob()).waitMove(point);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED_MOVE
    public static StatusCode move(haven.Coord point) {
        if (sendClickMessage_(point) == SC_INTERRUPTED) return SC_INTERRUPTED;
        return new MoveManager(Self.gob()).waitMove(point);
    }

    // etc
    public static StatusCode moveNorthTile() {
        haven.Coord2d northTile = CoordinateHandler.northTile(Self.location());
        return move(northTile);
    }

    public static double distance(haven.Gob gob) {
        return Self.location().dist(GobHandler.location(gob));
    }

    public static StatusCode moveCenter() {
        haven.Coord2d center = CoordinateHandler.tileCenter(Self.location());
        return move(center);
    }

    // TODO implement wait lift /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    public static StatusCode lift(haven.Gob gob) {
        {
            final StatusCode result = carry_();
            if (result != SC_SUCCEEDED) return result;
        }
        {
            final StatusCode result = actClick_(gob);
            if (result != SC_SUCCEEDED) return result;
        }
        return SC_SUCCEEDED;
    }
/// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode actClick_(haven.Gob gob) {
        haven.Coord gobLocationInCoord = GobHandler.locationInCoord(gob);
        return WidgetMessageHandler.sendObjectClickMessage(
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
    private static StatusCode dig_() {
        return sendActMessage_(A_DIG);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode mine_() {
        return sendActMessage_(A_MINE);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode carry_() {
        return sendActMessage_(A_CARRY);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode destroy_() {
        return sendActMessage_(A_DESTROY);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode fish_() {
        return sendActMessage_(A_FISH);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode inspect_() {
        return sendActMessage_(A_INSPECT);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode repair_() {
        return sendActMessage_(A_REPAIR);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode crime_() {
        return sendActMessage_(A_CRIME);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode swim_() {
        return sendActMessage_(A_SWIM);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode tracking_() {
        return sendActMessage_(A_TRACKING);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode aggro_() {
        return sendActMessage_(A_AGGRO);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode shoot_() {
        return sendActMessage_(A_SHOOT);
    }

    // send message shadow
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode sendClickMessage_(haven.Coord2d point) {
        return sendClickMessage_(CoordinateHandler.convertCoord2dToCoord(point));
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode sendClickMessage_(haven.Coord point) {
        return WidgetMessageHandler.sendClickMessage(
                ObjectShadow.mapView(),
                Util.mapViewCenter(),
                point,
                IM_LEFT,
                IM_NONE);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode sendActMessage_(String action) {
        return WidgetMessageHandler.sendActMessage(ObjectShadow.gameUI().menu, action);
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private static StatusCode sendCancelActMessage_() {
        return WidgetMessageHandler.sendCancelActMessage();
    }
}
