package lmi.automation;

// import lmi pacakge
import lmi.LMIException;

public class MacroTemplate implements Runnable {
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
        System.out.println("[automation is terminating]");
    }
}
