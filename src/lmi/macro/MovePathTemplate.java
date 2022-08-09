package lmi.macro;

import lmi.collection.Array;

public class MovePathTemplate implements Runnable {
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
        // set your path here...
        // example:
        //  path_.append(Player.location());
    }

    private void clearPath() {
        if (path_ == null)
            path_ = new Array<haven.Coord2d>();
        path_.removeAll();
    }

    private void main() throws InterruptedException {
        // compose your move macro code here...
        // example:
        //  for (haven.Coord2d point : path_) {
        //      Player.mapClickLeftMouseButton(point, 0);
        //      Thread.sleep(1000);
        //  }
    }

    private void didRun() {
        System.out.println("[macro is terminating]");
    }
}
