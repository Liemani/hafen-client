package lmi.automation;

import haven.Gob;
import haven.Coord;

import lmi.*;
import lmi.AutomationManager.Automation;
import static lmi.Api.*;
import static lmi.Constant.*;
import static lmi.Constant.ExceptionType.*;
import static lmi.Constant.BoundingBox.*;
import static lmi.Constant.TimeOut.*;
import static lmi.Constant.Plan.*;
import static lmi.Constant.gfx.terobjs.*;

// Will not support horse-riding building
// But will support horse-riding using
public class BuildDryingFrame extends Automation {
    final int SELF_WIDTH = BW_HORSE;
    final int SELF_HEIGHT = BH_HORSE;
    final Coord BB_SELF = BB_HORSE;

    final int PADDING = 2;
    final int PASSAGE_WIDTH = SELF_WIDTH + PADDING;
    final int HEIGHT_SET = BW_DRYING_FRAME + PASSAGE_WIDTH + BW_DRYING_FRAME;

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
        Api.alert("건설 자재가 있는 공간을 선택해주세요");
        _inputArea = getArea();

        Api.alert("Drying Frame을 건설할 공간을 선택해주세요");
        _workingArea = getArea();

        _outputArea = _getOutputArea(_workingArea);

        _checkTooSmallAreaException();

        _root = Coord.of(_workingArea.origin)
            .assignAdd(_workingArea.size)
            .assignSubtract(BB_SELF);
        _firstLeaf = Coord.of(_outputArea.origin).assignAdd(BH_DRYING_FRAME / 2, BW_DRYING_FRAME / 2);

        _orderCoordMax = _calculateOrderCoordMax(_outputArea);
        _orderCoord = Coord.zero();

        _trunk = Coord.of(_root);
        _branch = Coord.zero();
        _leaf = Coord.zero();
    }

    private Rect _getOutputArea(Rect workingArea) {
        final Rect outputArea = new Rect(workingArea);
        if (SELF_HEIGHT > BH_DRYING_FRAME) {
            final int leftPadding = (SELF_HEIGHT - BH_DRYING_FRAME) / 2;
            outputArea.origin.x += leftPadding;
            outputArea.size.x -= leftPadding;
        }
        outputArea.size.assignSubtract(TILE_IN_COORD);

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
    /// - Throws:
    ///     - ET_MOVE
    ///     - ET_NO_PATH
    ///     - ET_NO_INPUT
    private void _main() {
        while (true) {
            try {
                _loop();
            } catch (LMIException e) {
                if (e.type == ET_NO_INPUT) {
                    Api.alert("추가 건설 자재를 기다려요");
                    Api.sleep(TO_WAIT);
                } else
                    throw e;
            }
        }
    }

    /// - Throws:
    ///     - ET_MOVE
    ///     - ET_NO_PATH
    ///     - ET_NO_INPUT
    ///     - ET_WINDOW_OPEN
    private void _loop() {
        _calculateNextLeaf();
        _bringMaterial();
        Api.pathfindMove(_root);
        Api.forceMove(_trunk);
        Api.forceMove(_branch);
        Api.build(P_DFRAME, _leaf, D_EAST);
        Api.forceMove(_trunk);
    }

    /// - Throws:
    ///     - ET_MOVE
    ///     - ET_NO_PATH
    ///     - ET_NO_INPUT
    private void _bringMaterial() {
        _takeItemFromStockpile(RN_STOCKPILE_BOUGH, 2);
        _takeItemFromStockpile(RN_STOCKPILE_BRANCH, 5);
        _takeItemFromContainer(lmi.Constant.nameSet_string, 2);
    }

    /// - Throws:
    ///     - ET_MOVE
    ///     - ET_NO_PATH
    ///     - ET_NO_INPUT
    private void _takeItemFromStockpile(String name, int count) {
        final Array<Gob> stockpileArray = Api.gobArrayWhere(gob ->
                _outputArea.contains(gob.location())
                && gob.resourceName().endsWith(name));
        Api.pathfindTakeItemFromStockpileArray(stockpileArray, count);
    }

    private Array<Gob> getContainerGobArrayInOutputArea() {
        return Api.gobArrayWhere(gob ->
                _outputArea.contains(gob.location())
                && gob.isContainer());
    }

    /// - Throws:
    ///     - ET_MOVE
    ///     - ET_NO_PATH
    ///     - ET_NO_INPUT
    private void _takeItemFromContainer(String[] nameSet, int count) {
        final Array<Gob> containerArray = Api.gobArrayWhere(gob ->
                _outputArea.contains(gob.location())
                && Util.nameSet_includesResourcePath(
                    nameSet_container,
                    gob.resourceName()));
        Api.pathfindTakeItemFromContainerArray(
                containerArray,
                nameSet,
                count);
    }

    /// Set path tree by orderCoord and increase orderCoord
    /// - Throws
    ///     - ET_FULL_OUTPUT
    private void _calculateNextLeaf() {
        final Array<Gob> gobArrayInOutputArea = Api.gobArrayIn(_outputArea);
        while (true) {
            if (_orderCoord.y == _orderCoordMax.y) {
                if (gobArrayInOutputArea.count() == _orderCoordMax.x * _orderCoordMax.y)
                    throw new LMIException(ET_FULL_OUTPUT);
                else
                    _orderCoord.init(0, 0);
            }
            _calculateLeaf();
            _calculateNextOrderCoord();
            if (!gobArrayInOutputArea.containsWhere(gob -> gob.isAt(_leaf))) return;
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
            .assignAdd(0, PASSAGE_WIDTH * ((_orderCoord.y + 1) / 2));
        _branch.init(_leaf);
        _branch.y += ((BW_DRYING_FRAME + PASSAGE_WIDTH) / 2)
            * ((_orderCoord.y % 2 == 0) ? 1 : -1);
        _trunk.y = _branch.y;
    }

    private void _didRun(LMIException e) {
        switch (e.type) {
            case ET_INTERRUPTED:
                Api.alert("작업을 중단해요");
                break;
            case ET_FULL_OUTPUT:
                Api.alert("Drying Frame 건설을 완료했어요");
                break;
            case ET_TOO_SMALL_SPACE:
                Api.alert("선택한 작업 공간이 너무 좁아요");
                Api.message("통나무를 정리할 공간을 더 넓게 선택해 주세요");
                break;
            case ET_MOVE:
                Api.alert("예상치 못한 장애물에 가로막혔어요");
                break;
            case ET_LIFT:
                Api.alert("통나무를 들 수 없었어요");
                break;
            case ET_PUT:
                Api.alert("통나무를 내려놓을 수 없었어요");
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
