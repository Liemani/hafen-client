package lmi.api;

import haven.Gob;
import haven.Coord;

import lmi.Constant.StatusCode;
import static lmi.Constant.StatusCode.*;
import static lmi.Constant.Action.Custom.*;
import static lmi.Constant.TimeOut.*;

public class GobManager {
    // field
    final private Gob gob_;

    // constructor
    public GobManager(Gob gob) { gob_ = gob; }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED_MOVE
    public StatusCode waitMove(Coord destination) {
        while (!didArrive_(destination)) {
            switch (waitMoveBeginning()) {
                case SC_SUCCEEDED: break;
                case SC_INTERRUPTED: return SC_INTERRUPTED;
                case SC_FAILED_MOVE: return didArrive_(destination) ? SC_SUCCEEDED : SC_FAILED_MOVE;
                default:
                    new Exception().printStackTrace();
                    return SC_INTERRUPTED;
            }
            if (waitMoveEnding() == SC_INTERRUPTED) return SC_INTERRUPTED;
        }
        return SC_SUCCEEDED;
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED_MOVE
    public StatusCode waitMove() {
        final StatusCode result = waitMoveBeginning();
        if (result != SC_SUCCEEDED) return result;
        return waitMoveEnding();
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED_MOVE
    StatusCode waitMoveBeginning() {
        if (isMoving_()) return SC_SUCCEEDED;
        switch (WaitManager.waitTimeOut(gob_, AC_MOVE_DID_BEGIN, TO_TEMPORARY)) {
            case SC_SUCCEEDED: return SC_SUCCEEDED;
            case SC_INTERRUPTED: return SC_INTERRUPTED;
            case SC_TIME_OUT: return isMoving_() ? SC_SUCCEEDED : SC_FAILED_MOVE;
            default:
                new Exception().printStackTrace();
                return SC_INTERRUPTED;
        }
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    StatusCode waitMoveEnding() {
        while (true) {
            if (!isMoving_()) return SC_SUCCEEDED;
            switch (WaitManager.waitTimeOut(gob_, AC_MOVE_DID_END, TO_GENERAL)) {
                case SC_SUCCEEDED: return SC_SUCCEEDED;
                case SC_INTERRUPTED: return SC_INTERRUPTED;
                case SC_TIME_OUT: break;
                default:
                    new Exception().printStackTrace();
                    return SC_INTERRUPTED;
            }
        }
    }

    // lift
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED_LIFT
    public StatusCode waitLift(Gob gob) {
        if (gob_.isLifting(gob)) return SC_SUCCEEDED;
        switch (WaitManager.waitTimeOut(gob_, AC_DID_LIFT, TO_TEMPORARY)) {
            case SC_SUCCEEDED: return SC_SUCCEEDED;
            case SC_INTERRUPTED: return SC_INTERRUPTED;
            case SC_TIME_OUT: return gob_.isLifting(gob) ? SC_SUCCEEDED : SC_FAILED_LIFT;
            default:
                new Exception().printStackTrace();
                return SC_INTERRUPTED;
        }
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED_PUT
    public StatusCode waitPut() {
        if (!gob_.isLifting()) return SC_SUCCEEDED;
        switch (WaitManager.waitTimeOut(gob_, AC_DID_PUT, TO_TEMPORARY)) {
            case SC_SUCCEEDED: return SC_SUCCEEDED;
            case SC_INTERRUPTED: return SC_INTERRUPTED;
            case SC_TIME_OUT: return !gob_.isLifting() ? SC_SUCCEEDED : SC_FAILED_PUT;
            default:
                new Exception().printStackTrace();
                return SC_INTERRUPTED;
        }
    }

    // private method
    private boolean isMoving_() { return gob_.isMoving(); }
    private boolean didArrive_(Coord coord) { return gob_.isAt(coord); }
}
