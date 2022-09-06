package lmi.automation;

import lmi.Array;

import lmi.Self;
import static lmi.Constant.*;

public class Patrol000 implements Runnable {
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
        path_.append(Self.location());
        path_.append(Self.location().add(TILE_IN_COORD, 0));
    }

    private void clearPath() {
        if (path_ == null)
            path_ = new Array<haven.Coord>();
        path_.removeAll();
    }

    public void patrolPath() throws InterruptedException {
        for (haven.Coord location : path_) {
            Self.move(location);
            Thread.sleep(1000);
        }
    }
}
