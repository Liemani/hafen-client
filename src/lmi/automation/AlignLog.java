package lmi.automation;

// import haven.package
import haven.Gob;
import haven.Coord;

// import lmi
import lmi.*;

// import constant
import static lmi.Constant.ExceptionType.*;
import static lmi.Constant.gfx.terobjs.trees.*;
import static lmi.Constant.BoundingBox.*;
import static lmi.Constant.TimeOut.*;

public class AlignLog implements Runnable {
    private Rect _input;
    private Rect _output;

    private Coord _origin;

    private Coord _matrixSize;
    private Coord _previousMatrix;
    private Coord _currentMatrix;
    private Coord _targetPutCoord;
    private Coord _targetMoveCoord;

    private Coord _moveCoord;

    private Array<Gob> _logArrayToCarry;

    // Run
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
        Util.printAlertMessage("정리할 통나무가 있는 곳을 선택해주세요");
        _input = ClickManager.getArea();

        Util.printAlertMessage("통나무를 정리해 놓을 곳을 선택해주세요");
        _output = ClickManager.getArea();

        _checkAreaException();
        _init();
    }

    /// - Throws:
    ///     - ET_NO_SPACE_LEFT
    private void _checkAreaException() {
        _tooSmallAreaException();
    }

    private void _tooSmallAreaException() {
        if (_output.width() < BW_OLDTRUNK || _output.height() < BH_OLDTRUNK)
            throw new LMIException(ET_NO_SPACE_LEFT);
    }

    // Initialize
    private void _init() {
        _initOrigin();
        _initMatrixSize();
        _initPreviousMatrix();
        _initMatrix();
        _initTargetPutCoord();
        _initTargetMoveCoord();
        _initMoveCoord();
    }

    private void _initOrigin() {
        _origin = Coord.of(_output.maxX(), _output.maxY())
            .assignAdd(BB_BODY.divide(2));
    }

    private void _initMatrixSize() {
        final int rowSet = BH_OLDTRUNK + BH_BODY + BH_OLDTRUNK;
        final int x = _output.width() / BW_OLDTRUNK;
        final int y = (_output.height() / rowSet) * 2
            + ((_output.height() % rowSet >= BH_OLDTRUNK) ? 1 : 0);
        _matrixSize = Coord.of(x, y);
    }

    private void _initPreviousMatrix() { _previousMatrix = new Coord(); }
    private void _initMatrix() { _currentMatrix = Coord.zero(); }

    private void _initTargetPutCoord() {
        _targetPutCoord = new Coord();
        _calculateTargetPutCoord();
    }

    private void _initTargetMoveCoord() {
        _targetMoveCoord = new Coord();
        _calculateTargetMoveCoord();
    }

    private void _initMoveCoord() { _moveCoord = Coord.of(_origin); }

    // Main
    private void _main() {
        if (_coordIsPossessed(_targetPutCoord))
            _calculateNextCoord();

        while (true) {
            try {
                _loop();
            } catch (LMIException e) {
                if (e.type() != ET_NO_WORK_TO_DO) throw e;
                Util.printAlertMessage("추가 통나무를 기다려요");
                Util.sleep(TO_WAIT);
            }
        }
    }

    private void _loop() {
        _forceMove(_origin);
        _forceLift(_findLogToCarry());
        _forceMove(_origin);
        while (true) {
            _forceMove(_moveCoord.init(_moveCoord.x, _targetMoveCoord.y));
            while (true) {
                _forceMove(_moveCoord.init(_targetMoveCoord.x, _moveCoord.y));
                try {
                    Self.put(_targetPutCoord);
                    _calculateNextCoord();
                    break;
                } catch (LMIException e) {
                    if (e.type() != ET_PUT) throw e;
                    _calculateNextCoord();
                    if (_currentMatrix.y != _previousMatrix.y) {
                        _forceMove(_moveCoord.init(_origin.x, _moveCoord.y));
                        break;
                    }
                }
            }
            if (!Self.gob().isLifting()) break;
        }
        _forceMove(_moveCoord.init(_origin.x, _moveCoord.y));
        _forceMove(_origin);
    }

    private void _forceMove(Coord coord) { Self.forceMove(coord, TO_RETRY); }
    private void _forceLift(Gob gob) { Self.forceLift(gob, TO_RETRY); }

    /// - Throws
    ///     - ET_NO_WORK_TO_DO
    private Gob _findLogToCarry() {
        _logArrayToCarry = GobManager.gobArrayInArea(_input)
            .compactMap(gob -> {
                    final String resourceName = gob.resourceName();
                    if (resourceName == null) {
                        Util.debugPrint("gob: " + gob);
                        Util.debugPrint("gob is virtual: " + gob.virtual);
                        Util.debugPrint("class name of gob: " + gob.getClass().getName());
                        for (haven.GAttrib attribute : gob.attributeMap().values())
                            Util.debugPrint("name of attribute" + attribute.getClass().getName());
                    }
                    return resourceName.endsWith(RN_LOG) ? gob : null;
                    });

        if (_logArrayToCarry.isEmpty())
            throw new LMIException(ET_NO_WORK_TO_DO);

        return GobManager.closestGob(_logArrayToCarry);
    }

    /// - Throws
    ///     - ET_NO_SPACE_LEFT
    private void _calculateNextCoord() {
        while (true) {
            _calculateNextMatrix();
            _calculateTargetPutCoord();
            _calculateTargetMoveCoord();
            if (!_coordIsPossessed(_targetPutCoord)) break;
        }
    }

    /// - Throws
    ///     - ET_NO_SPACE_LEFT
    private void _calculateNextMatrix() {
        _previousMatrix.init(_currentMatrix);

        ++_currentMatrix.x;
        if (_currentMatrix.x == _matrixSize.x) {
            _currentMatrix.x = 0;
            ++_currentMatrix.y;
        }

        if (_currentMatrix.y == _matrixSize.y)
            throw new LMIException(ET_NO_SPACE_LEFT);
    }

    private void _calculateTargetPutCoord() {
        _targetPutCoord.init(_output.origin)
            .assignAdd(_currentMatrix.multiply(BB_OLDTRUNK))
            .assignAdd(BB_OLDTRUNK.divide(2))
            .assignAdd(0, ((_currentMatrix.y + 1) / 2) * BH_BODY);
    }

    private void _calculateTargetMoveCoord() {
        _targetMoveCoord.init(_targetPutCoord);
        _targetMoveCoord.y += ((BH_OLDTRUNK + BH_BODY) / 2)
            * ((_currentMatrix.y % 2 == 0) ? 1 : -1);
    }

    private boolean _coordIsPossessed(Coord coord) {
        java.util.ArrayList<Gob> gobArray = GobManager.gobArray();
        for (Gob gob : gobArray) {
            if (gob.isAt(coord))
                return true;
        }
        return false;
    }

    private void _didRun(LMIException e) {
        if (e == null) {
            Util.printAlertMessage("모든 통나무를 다 정리했어요");
            return;
        }

        switch (e.type()) {
            case ET_INTERRUPTED:
                Util.printAlertMessage("작업이 중단됐어요");
                break;
            case ET_NO_SPACE_LEFT:
                Util.printAlertMessage("통나무를 정리할 남은 공간이 없어요");
                break;
            case ET_NO_WORK_TO_DO:
                Util.printAlertMessage("모든 통나무를 다 정리했어요");
                break;
            default:
                throw e;
        }
    }
}
