package lmi.automation;

import lmi.Self;
import lmi.Array;

import static lmi.Constant.*;

import static lmi.Constant.*;

public class Patrol002 implements Runnable {
    private Array<haven.Coord> _path;

    public void run() {
        try {
            willRun();
            main();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // private methods
    private void willRun() {
        clearPath();
    }

    private void clearPath() {
        if (_path == null)
            _path = new Array<haven.Coord>();
        _path.removeAll();
    }

    private void main() throws InterruptedException {
        haven.Coord firstPoint = Self.location().add(0, -TILE_IN_COORD);
        haven.Coord secondPoint = Self.location().add(TILE_IN_COORD, 0);
        lmi.Api.move(firstPoint);
        Thread.sleep(2000);
        lmi.Api.move(secondPoint);
        Thread.sleep(2000);
    }
}
