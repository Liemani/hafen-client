package lmi.automation;

import lmi.Array;

public class MovePathTemplate implements Runnable {
    private Array<haven.Coord2d> path_;

    public void run() {
        willRun();
        main();
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

    private void main() {
        // compose your move automation code here...
        // example:
        //  for (haven.Coord2d point : path_) {
        //      Player.mapClickLeftMouseButton(point, 0);
        //      Thread.sleep(1000);
        //  }
    }

    private void didRun() {
        System.out.println("[automation is terminating]");
    }
}
