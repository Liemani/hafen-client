package lmi.automation;

import haven.Gob;
import haven.Coord;

import lmi.*;
import static lmi.Api.*;

public class Template implements Runnable {
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
        // compose your initial setting here...
    }

    private void _main() {
        // compose your automation code here...
    }

    private void _didRun(LMIException e) {
        if (e == null) {
            alert("작업을 정상적으로 완료했어요");
            return;
        }

        switch (e.type()) {
            case ET_INTERRUPTED:
                alert("작업을 중단했어요");
                break;
            // compose your exception case here...
            default:
                throw e;
        }
    }

    public static String man() {
        return
            "man page를 작성해 주세요" +
            "";
    }
}
