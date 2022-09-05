package lmi.automation;

import lmi.collection.Array;

import static lmi.Constant.*;

public class Patrol001 implements Runnable {
    private Array<haven.Coord> path_;

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
        path_.append(lmi.api.Self.location());
        path_.append(lmi.api.Self.location().add(0, TILE_IN_COORD));
    }

    private void clearPath() {
        if (path_ == null)
            path_ = new Array<haven.Coord>();
        path_.removeAll();
    }

    public void patrolPath() throws InterruptedException {
        for (haven.Coord location : path_) {
            lmi.api.Self.move(location);
            Thread.sleep(1000);
        }
    }
}
