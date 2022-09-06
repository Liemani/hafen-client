package lmi.automation;

import lmi.Array;

import lmi.Self;
import static lmi.Constant.*;

public class Patrol000 implements Runnable {
    private Array<haven.Coord> _path;

    public void run() {
        willRun();

        try {
            while (!Thread.interrupted())
                patrolPath();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // private methods
    private void willRun() {
        clearPath();
        _path.append(Self.location());
        _path.append(Self.location().add(TILE_IN_COORD, 0));
    }

    private void clearPath() {
        if (_path == null)
            _path = new Array<haven.Coord>();
        _path.removeAll();
    }

    public void patrolPath() throws InterruptedException {
        for (haven.Coord location : _path) {
            Self.move(location);
            Thread.sleep(1000);
        }
    }
}
