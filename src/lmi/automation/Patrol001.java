package lmi.automation;

import lmi.Array;

import lmi.Self;
import static lmi.Constant.*;

public class Patrol001 implements Runnable {
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
        _path.append(Self.location().add(0, TILE_IN_COORD));
    }

    private void clearPath() {
        if (_path == null)
            _path = new Array<haven.Coord>();
        _path.removeAll();
    }

    public void patrolPath() throws InterruptedException {
        for (haven.Coord location : _path) {
            lmi.Api.move(location);
            Thread.sleep(1000);
        }
    }
}
