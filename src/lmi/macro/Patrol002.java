package lmi.macro;

import lmi.collection.Array;

public class Patrol002 implements Runnable {
    private Array<haven.Coord2d> path_;

    public void run() {
        try {
            willRun();
            main();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) { e.printStackTrace(); }

        didRun();
    }

    // private methods
    private void willRun() {
        clearPath();
    }

    private void clearPath() {
        if (path_ == null)
            path_ = new Array<haven.Coord2d>();
        path_.removeAll();
    }

    private void main() throws InterruptedException {
        haven.Coord firstPoint = lmi.api.CoordinateHandler.convertCoord2dToCoord(lmi.api.Self.location().add(0.0, -11.0));
        haven.Coord secondPoint = lmi.api.CoordinateHandler.convertCoord2dToCoord(lmi.api.Self.location()).add(1, 0);
        lmi.api.Self.moveByIntCoordinate(firstPoint);
        Thread.sleep(2000);
        lmi.api.Self.moveByIntCoordinate(secondPoint);
        Thread.sleep(2000);
    }

    private void didRun() {
        System.out.println("[macro is terminating]");
    }
}
