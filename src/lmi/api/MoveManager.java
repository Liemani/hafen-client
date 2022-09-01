package lmi.api;

import lmi.Constant.StatusCode;
import static lmi.Constant.StatusCode.*;
import static lmi.Constant.Command.Custom.*;
import static lmi.Constant.TimeOut.*;

class MoveManager {
    // field
    haven.Gob gob_ = null;

    // constructor
    MoveManager(haven.Gob gob) { gob_ = gob; }

    // package method
    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    ///     - SC_FAILED_MOVE
    StatusCode waitMove(haven.Coord2d destination) {
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
    StatusCode waitMove(haven.Coord destination) {
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
    StatusCode waitMove() {
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
        switch (WaitManager.waitTimeOut(CC_SELF_MOVE_DID_BEGIN, TO_TEMPORARY)) {
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
            switch (WaitManager.waitTimeOut(CC_SELF_MOVE_DID_END, TO_GENERAL)) {
                case SC_SUCCEEDED: return SC_SUCCEEDED;
                case SC_INTERRUPTED: return SC_INTERRUPTED;
                case SC_TIME_OUT: break;
                default:
                    new Exception().printStackTrace();
                    return SC_INTERRUPTED;
            }
        }
    }

    // private method
    private boolean isMoving_() {
        return gob_.getattr(haven.Moving.class) != null;
    }

    public boolean didArrive_(haven.Coord2d point) {
        return GobHandler.isAt(gob_, point);
    }

    public boolean didArrive_(haven.Coord point) {
        return GobHandler.isAt(gob_, point);
    }
}
