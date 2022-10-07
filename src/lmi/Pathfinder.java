//  - Warnings:
//      - this run() modify argument
package lmi;

import java.util.TreeMap;
import java.util.LinkedList;

import haven.Gob;
import haven.Coord;

import static lmi.Constant.*;
import static lmi.Constant.ExceptionType.*;

class Pathfinder {
    private static Coord _mapOrigin;
    private static boolean[][] _map;
    private static Coord _destination;

    private static Coord[][] _directionMap;
    private static TreeMap<Integer, LinkedList<Coord>> _searchPriorityMap;

    private static Coord _origin;

    private static Coord _currentMoveMapLocation;
    private static Coord _lastMoveLocation;

    // Initialize
    static void init() {
        _map = new boolean[PURE_TILE_SIDE][PURE_TILE_SIDE];
        _directionMap = new Coord[PURE_TILE_SIDE][PURE_TILE_SIDE];
        _searchPriorityMap = new TreeMap<Integer, LinkedList<Coord>>();
    }

    // Getter
    private static boolean _isBlocked(Coord coord) { return _map[coord.x][coord.y]; }
    private static Coord _getDirection(Coord coord) { return _directionMap[coord.x][coord.y]; }

    // Setter
    private static void _setDirection(Coord coord, Coord direction) { _directionMap[coord.x][coord.y] = direction; }

    private static void _addDistanceMap(Coord coord) {
        final int rectilinearDistance = _origin.rectilinearDistance(coord);
        LinkedList<Coord> list = _searchPriorityMap.get(rectilinearDistance);
        if (list == null) {
            list = new LinkedList<Coord>();
            _searchPriorityMap.put(rectilinearDistance, list);
        }
        list.add(coord);
    }

    // Move
    /// - Throws:
    ///     - ET_MOVE
    static void move(Coord destination) {
        // if without moveCenter(), object could not loaded proper
        Api.moveCenter();
        _setMap();
        _destination = _transformMapCoord(destination);

        try {
            _findAndMove();
        } catch (Exception e) {
            throw e;
        } finally {
            _clear();
        }
    }

    static void move(Gob gob) {
        // if without moveCenter(), object could not loaded proper
        Api.moveCenter();
        _setMap();

        final Coord gobLocation = _transformMapCoord(gob.location());
        for (Coord direction : Coord.uecw) {
            _destination = gobLocation.add(direction);

            try {
                _findAndMove();
                _clear();
                return;
            } catch (LMIException e) {
                if (e.type() != ET_NO_PATH) {
                    _clear();
                    throw e;
                } else continue;
            }
        }

        _clear();
        throw new LMIException(ET_NO_PATH);
    }

    // Set Map
    private static void _setMap() {
        _mapOrigin = _calculateMapOrigin();
        _scanMap(_mapOrigin);

        _currentMoveMapLocation = Coord.zero();
        _lastMoveLocation = Coord.zero();
    }

    private static Coord _calculateMapOrigin() {
        final Coord targetLocation = Self.location().assignSubtract(TILE_IN_COORD * CHUNK_SIDE * (PURE_CHUNK_SIDE / 2));
        final int set = TILE_IN_COORD * CHUNK_SIDE;
        final int x = ((targetLocation.x - ((targetLocation.x < 0) ? (set - 1) : 0)) / set) * set;
        final int y = ((targetLocation.y - ((targetLocation.y < 0) ? (set - 1) : 0)) / set) * set;
        return Coord.of(x, y);
    }

    private static void _scanMap(Coord mapOrigin) {
        final Array<Gob> obstacleArray = _getObstaclArray();
        for (Gob gob : obstacleArray) {
            final Coord mapCoord = gob.location()
                .assignSubtract(_mapOrigin)
                .assignDivide(TILE_IN_COORD);
            try {
                _map[mapCoord.x][mapCoord.y] = true;
            } catch(ArrayIndexOutOfBoundsException e) { }
        }

        final Coord origin = _transformMapCoord(Self.location());
        _map[origin.x][origin.y] = false;
    }

    private static Array<Gob> _getObstaclArray() {
        return Api.gobArrayWhere(gob -> !gob.resourceName().startsWith("gfx/terobjs/plants/"));
    }

    // Find Path
    /// - Throws:
    ///     - ET_NO_PATH
    ///     - ET_MOVE
    private static void _findAndMove() {
        while (true) {
            _findPath();

            _printMap();

            try {
                _pathMove();
                break;
            } catch (LMIException e) {
                if (e.type() != ET_MOVE) throw e;
                _correct();
            }
        }
    }

    // Find Path
    /// - Throws:
    ///     - ET_NO_PATH
    private static void _findPath() {
        if (_map[_destination.x][_destination.y]) throw new LMIException(ET_NO_PATH);
        _reset();
        while (true) {
            final int rectilinearDistance = _searchPriorityMap.firstKey();
            final LinkedList<Coord> list = _searchPriorityMap.remove(rectilinearDistance);
            for (Coord node : list)
                _search(node);
            if (_searchPriorityMap.size() == 0) throw new LMIException(ET_NO_PATH);
            if (_directionMap[_origin.x][_origin.y] != null) break;
        }
    }

    private static void _reset() {
        for (int i = 0; i < PURE_TILE_SIDE; ++i)
            for (int j = 0; j < PURE_TILE_SIDE; ++j)
                _directionMap[i][j] = null;
        _searchPriorityMap.clear();

        _origin = _transformMapCoord(Self.location());
        _setDirection(_destination, Coord.ZERO);
        _addDistanceMap(_destination);
    }

    private static void _search(Coord node) {
        for (Coord direction : Coord.uecw) {
            final Coord leaf = node.add(direction);

            try {
                if (_getDirection(leaf) != null) continue;
            } catch (ArrayIndexOutOfBoundsException e) {
                continue;
            }
            if (_isBlocked(leaf)) continue;

            _setDirection(leaf, direction);
            _addDistanceMap(leaf);
        }
    }

    // Print Map
    private static void _printMap() {
        StringBuilder description = new StringBuilder();

        for (int i = 0; i < _map.length; ++i) {
            for (int j = 0; j < _map[0].length; ++j) {
                if (_map[j][i]) {
                    description.append('X');
                    continue;
                }
                final Coord direction = _directionMap[j][i];
                if (direction == Coord.uecw[0])
                    description.append('v');
                else if (direction == Coord.uecw[1])
                    description.append('<');
                else if (direction == Coord.uecw[2])
                    description.append('^');
                else if (direction == Coord.uecw[3])
                    description.append('>');
                else
                    description.append('.');
            }
            description.append('\n');
        }

        System.out.println(description);
    }

    // Path Move
    /// - Throws:
    ///     - ET_MOVE
    private static void _pathMove() {
        Coord direction = _getDirection(_origin);
        if (direction == null) {
            System.out.println("pathfind failed");
            return;
        }

        _currentMoveMapLocation.assign(_origin);
        _lastMoveLocation.assign(Self.location());
        _origin.assignSubtract(direction);
        Coord previousDirection = direction;
        direction = _getDirection(_origin);
        while (true) {
            if (direction != previousDirection) {
                _currentMoveMapLocation.assign(_origin);
                final Coord targetLocation = _origin.multiply(TILE_IN_COORD).assignAdd(_mapOrigin).assignAdd(TILE_IN_COORD / 2);
                Api.move(targetLocation);
                _lastMoveLocation.assign(targetLocation);
                if (_origin.equals(_destination))
                    break;
            }
            _origin.assignSubtract(direction);
            previousDirection = direction;
            direction = _getDirection(_origin);
        }
    }

    // Correct Map
    /// - Throws:
    ///     - ET_MOVE
    private static void _correct() {
        if (_transformMapCoord(Self.location()).equals(_currentMoveMapLocation)) {
            _map[_currentMoveMapLocation.x][_currentMoveMapLocation.y] = true;
        } else {
            final int clockwiseOrder = (int)Math.floor((Self.direction() + Math.PI / 4) / (Math.PI / 2)) % 4;
            final int clockwiseOrderFromSouth = (clockwiseOrder + 3) % 4;
            final Coord previousCoord = Self.location()
                .assignAdd(Coord.uecw[clockwiseOrderFromSouth]
                        .multiply(512));
            final Coord blockedMapCoord = _transformMapCoord(Coord.of(previousCoord))
                .assignSubtract(Coord.uecw[clockwiseOrderFromSouth]);
            _map[blockedMapCoord.x][blockedMapCoord.y] = true;
        }

        Api.move(_lastMoveLocation);
    }

    // Clear
    private static void _clear() {
        for (int i = 0; i < PURE_TILE_SIDE; ++i)
            for (int j = 0; j < PURE_TILE_SIDE; ++j) {
                _map[i][j] = false;
                _directionMap[i][j] = null;
            }
        _searchPriorityMap.clear();

        _mapOrigin = null;
        _origin = null;
        _destination = null;

        _currentMoveMapLocation = null;
        _lastMoveLocation = null;
    }

    // Etc
    private static Coord _transformMapCoord(Coord coord) {
            return coord
                .assignSubtract(_mapOrigin)
                .assignDivide(TILE_IN_COORD);
    }
}
