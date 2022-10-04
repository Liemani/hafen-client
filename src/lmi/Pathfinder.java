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

    // Run
    static void run(Coord destination) {
        // if without moveCenter(), object could not loaded proper
        Api.moveCenter();
        _setMap(destination);

        while (true) {
            _findPath();
            _printMap();

            try {
                _pathMove();
                break;
            } catch (LMIException e) {
                if (e.type() != ET_MOVE) {
                    _clear();
                    throw e;
                }
                _correct();
            }
        }
        _clear();
    }

    // Set Map
    private static void _setMap(Coord destination) {
        _mapOrigin = _calculateMapOrigin();
        _scanMap(_mapOrigin);
        _destination = _assignCalculateMapCoord(_mapOrigin, destination);
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

        final Coord origin = _assignCalculateMapCoord(mapOrigin, Self.location());
        _map[origin.x][origin.y] = false;
    }

    private static Array<Gob> _getObstaclArray() {
        return Api.gobArrayWhere(gob -> !gob.resourceName().startsWith("gfx/terobjs/plants/"));
    }

    // Find Path
    /// - Throws:
    ///     - ET_NO_PATH
    private static void _findPath() {
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

        _origin = _assignCalculateMapCoord(_mapOrigin, Self.location());
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
    private static void _pathMove() {
        Coord direction = _getDirection(_origin);
        if (direction == null) {
            System.out.println("pathfind failed");
            return;
        }

        Api.moveCenter();
        _origin.assignSubtract(direction);
        Coord previousDirection = direction;
        direction = _getDirection(_origin);
        while (true) {
            if (_origin.equals(_destination)) {
                Api.move(_origin.multiply(TILE_IN_COORD).assignAdd(_mapOrigin).assignAdd(TILE_IN_COORD / 2));
                break;
            } else if (direction != previousDirection)
                Api.move(_origin.multiply(TILE_IN_COORD).assignAdd(_mapOrigin).assignAdd(TILE_IN_COORD / 2));
            _origin.assignSubtract(direction);
            previousDirection = direction;
            direction = _getDirection(_origin);
        }
    }

    // Correct Map
    private static void _correct() {
        final int clockwiseOrder = (int)Math.floor((Self.direction() + Math.PI / 4) / (Math.PI / 2)) % 4;
        final int clockwiseOrderFromSouth = (clockwiseOrder + 3) % 4;
        final Coord previousCoord = Self.location()
            .assignAdd(Coord.uecw[clockwiseOrderFromSouth]
                    .multiply(512));
        final Coord blockedMapCoord = _assignCalculateMapCoord(_mapOrigin, Coord.of(previousCoord))
            .assignSubtract(Coord.uecw[clockwiseOrderFromSouth]);
        _map[blockedMapCoord.x][blockedMapCoord.y] = true;

        Api.move(previousCoord);
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
    }

    // Etc
    private static Coord _assignCalculateMapCoord(Coord mapOrigin, Coord coord) {
            return coord
                .assignSubtract(_mapOrigin)
                .assignDivide(TILE_IN_COORD);
    }
}
