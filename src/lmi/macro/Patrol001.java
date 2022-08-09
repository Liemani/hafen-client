package lmi.macro;

import lmi.collection.Array;

public class Patrol001 implements Runnable {
    private Array<haven.Coord2d> path_;

    public void run() {
        willRun();

        try {
            while (!Thread.interrupted())
                patrolPath();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) { e.printStackTrace(); }

        didRun();
    }

    // private methods
    private void willRun() {
        clearPath();
        path_.append(Player.location());
        path_.append(Player.location().add(100, 100));
    }

    private void clearPath() {
        if (path_ == null)
            path_ = new Array<haven.Coord2d>();
        path_.removeAll();
    }

    public void patrolPath() throws InterruptedException {
        for (haven.Coord2d location : path_) {
            Player.mapClickLeftMouseButton(location, 0);
            Thread.sleep(1000);
        }
    }

    private void didRun() {
        System.out.println("[macro is terminating]");
    }
}
