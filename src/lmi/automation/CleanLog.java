package lmi.automation;

// import haven.package
import haven.Gob;
import haven.Coord;

// import lmi package
import lmi.*;

// import constant
import static lmi.Constant.ExceptionType.*;
import static lmi.Constant.gfx.terobjs.trees.*;
import static lmi.Constant.BoundingBox.*;

public class CleanLog implements Runnable {
    private Rect _logGetArea;
    private Rect _logDumpArea;

    private Coord _matrixSize;
    private Coord _matrixCoord;
    private Coord _targetCoord;
    private Coord _moveCoord;

    private Array<Gob> _logArrayToCarry;

    public void run() {
        LMIException result = null;
        try {
            _willRun();
            _main();
        } catch (LMIException e) {
            result = e;
        }
        _didRun(result);
    }

    // private methods
    private void _willRun() {
        System.out.println("정리할 log가 있는 곳을 선택해주세요");
        _logGetArea = ClickManager.getArea();

        System.out.println("로그를 정리해 놓을 곳을 선택해주세요");
        _logDumpArea = ClickManager.getArea();

        _checkAreaException();
        _init();
    }

    /// - Throws:
    ///     - ET_NO_SPACE_LEFT
    private void _checkAreaException() {
        _tooSmallAreaException();
    }

    private void _tooSmallAreaException() {
        if (_logDumpArea.size.y < BH_LOG)
            throw new LMIException(ET_NO_SPACE_LEFT);
    }

    // Initialize
    private void _init() {
        _initMatrixSize();
        _initMatrixCoord();
        _initTargetCoord();
    }

    private void _initMatrixSize() {
        final int x = _logDumpArea.width() / BW_LOG;
        final int rowSet = BH_LOG + BH_BODY + BH_LOG;
        final int y = (_logDumpArea.height() / rowSet) * 2
            + ((_logDumpArea.height() % rowSet >= BH_LOG) ? 1 : 0);
        _matrixSize = Coord.of(x, y);
    }

    private void _initMatrixCoord() {
        _matrixCoord = Coord.zero();
    }

    private void _initTargetCoord() {
        _targetCoord = _targetCoord();
    }

    private void _main() {
        while (true) {
            _loop();
        }
    }

    private void _loop() {
        final Gob logToCarry = _findLogToCarry();
        Self.lift(logToCarry);

        while (true) {
            if (!_coordIsPossessed(_targetCoord)) break;
            _setNextTargetCoord();
        }

        _moveCoord = Coord.of(_logDumpArea.maxX(), _logDumpArea.maxY())
            .assignAdd(BW_BODY / 2, BH_BODY / 2);
        _moveAgain(_moveCoord);

        _put();

        _moveCoord.x = _logDumpArea.maxX() + BW_BODY / 2;
        _moveAgain(_moveCoord);

        _moveCoord.y = _logDumpArea.maxY() + BH_BODY / 2;
        _moveAgain(_moveCoord);
    }

    private void _moveAgain(Coord coord) {
        while (true) {
            try {
                Self.move(coord);
                break;
            } catch (LMIException e) {
                if (e.type() != ET_MOVE) throw e;
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ee) {
                    throw new LMIException(ET_INTERRUPTED);
                }
            }
        }
    }

    /// - Throws
    ///     - ET_NO_WORK_TO_DO
    private Gob _findLogToCarry() {
        final Array<Gob> gobArrayInGetArea = GobManager.gobArrayInArea(_logGetArea);
        gobArrayInGetArea.removeAllWhere(gob -> !gob.resourceName().endsWith(RN_LOG));
        _logArrayToCarry = gobArrayInGetArea;
        // TODO map? 등을 호출해서 새로운 array를 생성하여 반환

        if (_logArrayToCarry.isEmpty())
            throw new LMIException(ET_NO_WORK_TO_DO);

        return GobManager.closestGob(_logArrayToCarry);
    }

    /// - Throws
    ///     - ET_NO_SPACE_LEFT
    private void _setNextRowColumnCoord() {
        ++_matrixCoord.x;
        if (_matrixCoord.x == _matrixSize.x) {
            _matrixCoord.x = 0;
            ++_matrixCoord.y;
        }

        if (_matrixCoord.y == _matrixSize.y)
            throw new LMIException(ET_NO_SPACE_LEFT);
    }

    private Coord _targetCoord() {
        int x = _logDumpArea.minX() + _matrixCoord.x * BW_LOG + BW_LOG / 2;
        int y = _logDumpArea.minY() + _matrixCoord.y * BH_LOG + BH_LOG / 2
            + ((_matrixCoord.y + 1) / 2) * BH_BODY;
        return Coord.of(x, y);
    }

    private void _setNextTargetCoord() {
        _setNextRowColumnCoord();
        _targetCoord = _targetCoord();
    }

    private boolean _coordIsPossessed(Coord coord) {
        java.util.ArrayList<Gob> gobArray = GobManager.gobArray();
        for (Gob gob : gobArray) {
            if (gob.isAt(coord))
                return true;
        }
        return false;
    }

    private void _put() {
        while (true) {
            if (Self.location().y - BH_BODY != _moveCoord.y) {
                _moveCoord.x = _logDumpArea.maxX() + BW_BODY / 2;
                _moveAgain(_moveCoord);

                _moveCoord.y = _targetCoord.y + BH_LOG / 2 + BH_BODY / 2;
                _moveAgain(_moveCoord);
            }

            _moveCoord.x = _targetCoord.x;
            _moveAgain(_moveCoord);

            try {
                Self.put(_targetCoord);
                return;
            } catch (LMIException e) {
                if (e.type() != ET_PUT) throw e;
            } finally {
                _setNextTargetCoord();
            }
        }
    }

    private void _didRun(LMIException e) {
        if (e == null) {
            System.out.println("모든 log를 정리했어요");
            return;
        }

        switch (e.type()) {
            case ET_INTERRUPTED:
                System.out.println("작업을 중단했어요");
                break;
            case ET_NO_SPACE_LEFT:
                System.out.println("log를 정리할 공간 더 이상 없어요");
                break;
            case ET_NO_WORK_TO_DO:
                System.out.println("더 정리할 log가 없어요");
                break;
            default:
                throw e;
        }
    }
}
