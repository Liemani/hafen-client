package lmi.automation;

// import haven
import haven.Coord;

// import lmi pacakge
import lmi.Array;
import lmi.LMIException;

public class MovePathTemplate implements Runnable {
    private Array<Coord> _path;

    public void run() {
        LMIException result = null;
        try {
            _willRun();
            _main();
        } catch (LMIException e) {
            result = e;
        }
        _didRun(result);
    }

    // private methods
    private void _willRun() {
        _path = new Array<Coord>();
        // set your path here...
        // example:
        //  _path.append(ClickManager.getCoord());
    }

    private void _main() {
        // compose your move automation code here...
        // example:
        //  for (Coord point : _path)
        //      Player.move(point);
    }

    private void _didRun(LMIException e) {
        System.out.println("[automation is terminating]");
    }
}
