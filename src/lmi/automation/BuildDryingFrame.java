package lmi.automation;

import haven.Gob;
import haven.Coord;

import lmi.*;
import lmi.AutomationManager.Automation;
import static lmi.Api.*;
import static lmi.Constant.ExceptionType.*;
import static lmi.Constant.BoundingBox.*;
import static lmi.Constant.TimeOut.*;

public class BuildDryingFrame extends Automation {
    final int SELF_WIDTH = BW_HORSE;
    final int SELF_HEIGHT = BH_HORSE;
    final Coord BB_SELF = BB_HORSE;
    final double SELF_DIAGONAL = BB_SELF.diagonal();

    final int PADDING = 2;
    final int ROUTE_WIDTH = SELF_WIDTH + PADDING;
    final int HEIGHT_SET = BW_DRYING_FRAME + ROUTE_WIDTH + BW_DRYING_FRAME;

    private Rect _inputArea;
    private Rect _workingArea;
    private Rect _outputArea;

    private Coord _orderCoordMax;
    private Coord _orderCoord;

    private Coord _root;
    private Coord _trunk;
    private Coord _branch;
    private Coord _firstLeaf;
    private Coord _leaf;

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
        alert("건설 자재가 있는 공간을 선택해주세요");
        _inputArea = getArea();

        alert("Drying Frame을 건설할 공간을 선택해주세요");
        _workingArea = getArea();

        _outputArea = _calculateOutputArea(_workingArea);

        _checkTooSmallAreaException();

        _root = Coord.of(_workingArea.origin)
            .assignAdd(_workingArea.size)
            .assignSubtract(SELF_DIAGONAL / 2);
        _firstLeaf = Coord.of(_outputArea.origin).assignAdd(BH_DRYING_FRAME / 2, BW_DRYING_FRAME / 2);

        _orderCoordMax = _calculateOrderCoordMax(_outputArea);
        _orderCoord = Coord.zero();

        _trunk = Coord.of(_root);
        _branch = Coord.zero();
        _leaf = Coord.zero();
    }

    private Rect _calculateOutputArea(Rect workingArea) {
        final double outputAreaToWorkingOrigin = SELF_DIAGONAL / Math.sqrt(2.0) / 2;

        final Rect outputArea = new Rect(workingArea);
        outputArea.origin.x += (SELF_HEIGHT - BH_DRYING_FRAME) / 2;
        outputArea.size
            .assignSubtract((SELF_HEIGHT - BH_DRYING_FRAME) / 2, 0)
            .assignSubtract(outputAreaToWorkingOrigin + SELF_DIAGONAL / 2);

        return outputArea;
    }

    /// - Throws:
    ///     - ET_TOO_SMALL_SPACE
    private void _checkTooSmallAreaException() {
        if (_outputArea.width() < BH_DRYING_FRAME || _outputArea.height() < BW_DRYING_FRAME)
            throw new LMIException(ET_TOO_SMALL_SPACE);
    }

    private Coord _calculateOrderCoordMax(Rect outputArea) {
        final int x = outputArea.width() / BH_DRYING_FRAME;
        final int y = (outputArea.height() / HEIGHT_SET) * 2
            + ((outputArea.height() % HEIGHT_SET >= BW_DRYING_FRAME) ? 1 : 0);
        return Coord.of(x, y);
    }

    // Main
    private void _main() {
        while (true) {
            try {
                _loop();
            } catch (LMIException e) {
                if (e.type() == ET_NO_INPUT) {
                    alert("추가 건설 자재를 기다려요");
                    sleep(TO_WAIT);
                } else
                    throw e;
            }
        }
    }

    private void _loop() {
        _calculateNextLeaf();
        final Gob targetLog = _logToCarry();
        forceMove(_root);
        forceLift(targetLog);
        forceMove(_root);
        forceMove(_trunk);
        forceMove(_branch);
        forcePut(_leaf);
        forceMove(_trunk);
    }

    /// - Throws
    ///     - ET_NO_INPUT
    private Gob _logToCarry() {
        Array<Gob> logArrayToCarry = gobArrayIn(_inputArea)
            .compactMap(gob -> gob.isLog() ? gob : null);

        if (logArrayToCarry.isEmpty())
            throw new LMIException(ET_NO_INPUT);

        return closestGobIn(logArrayToCarry);
    }

    /// - Throws
    ///     - ET_FULL_OUTPUT
    private void _calculateNextLeaf() {
        final Array<Gob> gobArray = gobArrayIn(_outputArea);
        while (true) {
            if (_orderCoord.y == _orderCoordMax.y) {
                if (gobArray.count() == _orderCoordMax.x * _orderCoordMax.y)
                    throw new LMIException(ET_FULL_OUTPUT);
                else
                    _orderCoord.init(0, 0);
            }
            _calculateLeaf();
            _calculateNextOrderCoord();
            if (!gobArray.containsWhere(gob -> gob.isAt(_leaf))) return;
        }
    }

    private void _calculateNextOrderCoord() {
        ++_orderCoord.x;
        if (_orderCoord.x == _orderCoordMax.x) {
            _orderCoord.x = 0;
            ++_orderCoord.y;
        }
    }

    private void _calculateLeaf() {
        _leaf.assign(_firstLeaf)
            .assignAdd(_orderCoord.multiply(BH_DRYING_FRAME, BW_DRYING_FRAME))
            .assignAdd(0, ROUTE_WIDTH * ((_orderCoord.y + 1) / 2));
        _branch.init(_leaf);
        _branch.y += ((BW_DRYING_FRAME + ROUTE_WIDTH) / 2)
            * ((_orderCoord.y % 2 == 0) ? 1 : -1);
        _trunk.y = _branch.y;
    }

    private void _didRun(LMIException e) {
        switch (e.type()) {
            case ET_INTERRUPTED:
                alert("작업을 중단해요");
                break;
            case ET_FULL_OUTPUT:
                alert("Drying Frame 건설을 완료했어요");
                break;
            case ET_TOO_SMALL_SPACE:
                alert("선택한 작업 공간이 너무 좁아요");
                message("통나무를 정리할 공간을 더 넓게 선택해 주세요");
                break;
            case ET_MOVE:
                alert("에상치 못한 장애물에 가로막혔어요");
                break;
            case ET_LIFT:
                alert("통나무를 들 수 없었어요");
                break;
            case ET_PUT:
                alert("통나무를 내려놓을 수 없었어요");
                break;
            default:
                throw e;
        }
    }

    public static String man() {
        return
            "BuildDryingFrame v0.1.0\n" +
            "설  명: Drying Frame을 건설합니다\n" +
            "\n" +
            "";
    }
}
