package lmi;

// import java.util
import java.util.ArrayList;
import java.util.function.Predicate;

// import haven
import haven.Gob;
import haven.Coord;

// import constant
import static lmi.Constant.ExceptionType.*;
import static lmi.Constant.Action.Custom.*;
import static lmi.Constant.TimeOut.*;

public class GobManager {
    // Field
    final private Gob _gob;

    // Constructor
    public GobManager(Gob gob) { _gob = gob; }

    // Move
    /// - Throws:
    ///     - ET_MOVE
    public void waitMove(Coord destination) {
        while (!_didArrive(destination)) {
            try {
                _waitMoveBeginning();
            } catch (LMIException e) {
                if (e.type() == ET_MOVE && _didArrive(destination)) return;
                else throw e;
            }
            _waitMoveEnding();
        }
    }

    /// - Throws:
    ///     - ET_MOVE
    public void waitMove() {
        _waitMoveBeginning();
        _waitMoveEnding();
    }

    /// - Throws:
    ///     - ET_MOVE
    private void _waitMoveBeginning() {
        if (_isMoving()) return;
        try {
            WaitManager.waitTimeOut(_gob, AC_MOVE_DID_BEGIN, TO_TEMPORARY);
        } catch (LMIException e) {
            if (e.type() != ET_TIME_OUT) throw e;
            if (!_isMoving()) throw new LMIException(ET_MOVE);
        }
    }

    private void _waitMoveEnding() {
        while (true) {
            if (!_isMoving()) return;
            try {
                WaitManager.waitTimeOut(_gob, AC_MOVE_DID_END, TO_GENERAL);
                break;
            } catch (LMIException e) {
                if (e.type() != ET_TIME_OUT) throw e;
            }
        }
    }

    // Lift
    /// - Throws:
    ///     - ET_LIFT
    public void waitLift(Gob gob) {
        if (_gob.isLifting(gob)) return;
        try {
            WaitManager.waitTimeOut(_gob, AC_DID_LIFT, TO_TEMPORARY);
        } catch (LMIException e) {
            if (e.type() != ET_TIME_OUT) throw e;
            if (!_gob.isLifting(gob)) throw new LMIException(ET_LIFT);
        }
    }

    /// - Throws:
    ///     - ET_PUT
    public void waitPut() {
        if (!_gob.isLifting()) return;
        try {
            WaitManager.waitTimeOut(_gob, AC_DID_PUT, TO_TEMPORARY);
        } catch (LMIException e) {
            if (e.type() != ET_TIME_OUT) throw e;
            if (_gob.isLifting()) throw new LMIException(ET_PUT);
        }
    }

    // Private Method
    private boolean _isMoving() { return _gob.isMoving(); }
    private boolean _didArrive(Coord coord) { return _gob.isAt(coord); }

    // Static Field
    private static ArrayList<Gob> _gobArrayList = new ArrayList<Gob>();

    // Accessing Static Filed
    public static void setGobArray(ArrayList<Gob> gobArray) { _gobArrayList = gobArray; }

    // Gob Array
    public static Array<Gob> gobArray() {
        Array<Gob> gobArray = new Array<Gob>();

        for (Gob gob : _gobArrayList) {
            if (gob.getClass() != Gob.class) continue;
            else gobArray.append(gob);
        }
        return gobArray;
    }

    public static Array<Gob> gobArrayWhere(Predicate<Gob> predicate) {
        Array<Gob> gobArray = new Array<Gob>();

        for (Gob gob : _gobArrayList) {
            if (gob.getClass() != Gob.class) continue;
            else if (predicate.test(gob)) gobArray.append(gob);
        }
        return gobArray;
    }

    public static Array<Gob> gobArrayInArea(Rect area) {
        return GobManager.gobArrayWhere(gob -> area.contains(gob.location()));
    }

    // Closest Gob
    public static Gob closestGob(Array<Gob> gobArray) {
        Gob closestGob = null;
        double distanceToClosestGob = Double.MAX_VALUE;

        for (Gob gob : gobArray) {
            final double distance = Self.distance(gob);
            if (distance < distanceToClosestGob) {
                closestGob = gob;
                distanceToClosestGob = distance;
            }
        }
        return closestGob;
    }
}
