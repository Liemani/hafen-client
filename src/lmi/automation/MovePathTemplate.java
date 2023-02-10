package lmi.automation;

import haven.Gob;
import haven.Coord;

import lmi.*;
import static lmi.Api.*;
import static lmi.Constant.TimeOut.*;

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
        //  _path.append(Self.location());
    }

    private void _main() {
        // compose your move automation code here...
        // example:
        //  for (Coord coord : _path)
        //      Self.forceMove(coord, TO_RETRY);
    }

    private void _didRun(LMIException e) {
        if (e == null) {
            alert("작업을 정상적으로 완료했어요");
            return;
        }

        switch (e.type) {
            case ET_INTERRUPTED:
                alert("작업을 중단했어요");
                break;
            // compose your exception case here...
            default:
                throw e;
        }
    }
}
