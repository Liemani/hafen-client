package lmi.automation;

// import haven.package
import haven.Gob;
import haven.Coord;

// import lmi package
import lmi.collection.Array;
import lmi.*;
import lmi.graphic.Rect;
import lmi.api.*;
import lmi.Constant.BoundingBox.*;
import lmi.Constant.StatusCode;

// import constant
import static lmi.Constant.StatusCode.*;
import static lmi.Constant.gfx.terobjs.trees.*;
import static lmi.Constant.BoundingBox.*;

public class CleanLog implements Runnable {
    private StatusCode result_;

    private Rect logGetArea_;
    private Rect logDumpArea_;

    private Coord matrixSize_;
    private Coord matrixCoord_;
    private Coord targetCoord_;
    private Coord moveCoord_;

    private Array<Gob> logArrayToCarry_;

    public void run() {
        willRun();
        if (result_ == SC_SUCCEEDED) main();
        didRun();
    }

    // private methods
    private void willRun() {
        System.out.println("정리할 log가 있는 곳을 선택해주세요");
        logGetArea_ = ClickManager.getArea();

        System.out.println("로그를 정리해 놓을 곳을 선택해주세요");
        logDumpArea_ = ClickManager.getArea();

        this.handleException();
        if (result_ != SC_SUCCEEDED) return;

        this.init();
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_FAILED_TOO_SMALL_AREA
    private void handleException() {
        result_ = tooSmallAreaException();
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_FAILED_TOO_SMALL_AREA
    private StatusCode tooSmallAreaException() {
        return (logDumpArea_.size.y < BH_LOG)
            ? SC_FAILED_TOO_SMALL_AREA
            : SC_SUCCEEDED;
    }

    // Initialize
    private void init() {
        initMatrixSize();
        initRowColumnCoord();
    }

    private void initMatrixSize() {
        final int x = logDumpArea_.width() / BW_LOG;
        final int rowSet = BH_LOG + BH_BODY + BH_LOG;
        final int y = (logDumpArea_.height() / rowSet) * 2
            + ((logDumpArea_.height() % rowSet >= BH_LOG) ? 1 : 0);
        matrixSize_ = Coord.of(x, y);
    }

    private void initRowColumnCoord() {
        matrixCoord_ = Coord.zero;
    }

    private void main() {
        while (!Thread.interrupted()) {
            loop();
            if (result_ != SC_SUCCEEDED) return;
        }
    }

    private void loop() {
        final Gob logToCarry = this.findLogToCarry();
        if (logToCarry == null) {
            result_ = SC_FINISH_NOTHING_TO_DO;
            return;
        }

        result_ = Self.lift(logToCarry);
        if (result_ != SC_SUCCEEDED) return;

        while (true) {
            targetCoord_ = this.targetCoord();
            if (!coordIsPossessed(targetCoord_)) break;
            if (setNextRowColumnCoord() == true) continue;

            result_ = SC_FINISH_NO_SPACE;
            return;
        }

        moveCoord_ = Coord.of(logDumpArea_.maxX(), logDumpArea_.maxY())
            .assignAdd(BW_BODY / 2, BH_BODY / 2);
        this.moveAgain(moveCoord_);
        if (result_ != SC_SUCCEEDED) return;

        this.put(targetCoord_);
        if (result_ != SC_SUCCEEDED) return;

        moveCoord_.x = logDumpArea_.maxX() + BW_BODY / 2;
        this.moveAgain(moveCoord_);
        if (result_ != SC_SUCCEEDED) return;

        moveCoord_.y = logDumpArea_.maxY() + BH_BODY / 2;
        this.moveAgain(moveCoord_);
        if (result_ != SC_SUCCEEDED) return;
    }

    /// - Returns:
    ///     - SC_SUCCEEDED
    ///     - SC_INTERRUPTED
    private void moveAgain(Coord coord) {
        while (true) {
            result_ = Self.move(coord);
            switch (result_) {
                case SC_SUCCEEDED: return;
                case SC_FAILED_MOVE:
                    try {
                        Thread.sleep(10000);
                    } catch (Exception e) {
                        result_ = SC_INTERRUPTED;
                        return;
                    }
                    continue;
                case SC_INTERRUPTED: return;
                default:
                    new Exception().printStackTrace();
                    return;
            }
        }
    }

    private Gob findLogToCarry() {
        this.setLogArrayToCarryFromGetArea();

        Gob closestLog = null;
        double closestDistance = Double.MAX_VALUE;

        for (Gob log : logArrayToCarry_) {
            final double distance = Self.distance(log);
            if (distance < closestDistance) {
                closestLog = log;
                closestDistance = distance;
            }
        }
        return closestLog;
    }

    private void setLogArrayToCarryFromGetArea() {
        logArrayToCarry_ = GobManager.getGobArrayInArea(logGetArea_);
        logArrayToCarry_.removeAllWhere(gob -> !gob.resourceName().endsWith(RN_LOG));
    }

    private boolean setNextRowColumnCoord() {
        ++matrixCoord_.x;
        if (matrixCoord_.x == matrixSize_.x) {
            matrixCoord_.x = 0;
            ++matrixCoord_.y;
        }
        return matrixCoord_.y != matrixSize_.y;
    }

    private Coord targetCoord() {
        int x = logDumpArea_.minX() + matrixCoord_.x * BW_LOG + BW_LOG / 2;
        int y = logDumpArea_.minY() + matrixCoord_.y * BH_LOG + BH_LOG / 2
            + ((matrixCoord_.y + 1) / 2) * BH_BODY;
        return Coord.of(x, y);
    }

    private boolean coordIsPossessed(Coord coord) {
        java.util.Iterator<Gob> iterator = lmi.api.Util.iterator();
        while (iterator.hasNext()) {
            final Gob gob = iterator.next();
            if (gob.getClass() != Gob.class)
                continue;
            if (gob.isAt(coord))
                return true;
        }
        return false;
    }

    private void put(Coord targetCoord) {
        while (true) {
            if (Self.location().y - BH_BODY != moveCoord_.y) {
                moveCoord_.x = logDumpArea_.maxX() + BW_BODY / 2;
                this.moveAgain(moveCoord_);
                if (result_ != SC_SUCCEEDED) return;

                moveCoord_.y = targetCoord.y + BH_LOG / 2 + BH_BODY / 2;
                this.moveAgain(moveCoord_);
                if (result_ != SC_SUCCEEDED) return;
            }

            moveCoord_.x = targetCoord.x;
            this.moveAgain(moveCoord_);
            if (result_ != SC_SUCCEEDED) return;

            result_ = Self.put(targetCoord);
            switch (result_) {
                case SC_SUCCEEDED:
                    if (this.setNextRowColumnCoord() != true)
                        result_ = SC_FINISH_NO_SPACE;
                    targetCoord_ = this.targetCoord();
                    return;
                case SC_INTERRUPTED: return;
                case SC_FAILED_PUT:
                    if (this.setNextRowColumnCoord() != true) {
                        result_ = SC_FINISH_NO_SPACE;
                        return;
                    }
                    targetCoord_ = this.targetCoord();
                    break;
                default:
                    new Exception().printStackTrace();
                    return;
            }
        }
    }

    private void didRun() {
        switch (result_) {
            case SC_INTERRUPTED:
                System.out.println("작업이 중지됐어요");
                break;
            case SC_FAILED_TOO_SMALL_AREA:
                System.out.println("log를 정리할 공간이 너무 작아요");
                break;
            case SC_FINISH_NOTHING_TO_DO:
                System.out.println("더 정리할 log가 없어요");
                break;
            case SC_FINISH_NO_SPACE:
                System.out.println("log를 정리할 공간 더 이상 없어요");
                break;
            default:
                System.out.println("log를 모두 정리했어요");
                break;
        }
        lmi.Util.debugPrint(this.getClass(), "result: " + result_);
    }
}
