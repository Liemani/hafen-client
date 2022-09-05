package lmi.automation;

public class MacroTemplate implements Runnable {
    public void run() {
        willRun();
        main();
        didRun();
    }

    // private methods
    private void willRun() {
        // compose your initial setting here...
    }

    private void main() {
        while (!Thread.interrupted()) {
            // compose your automation code here...
        }
    }

    private void didRun() {
        System.out.println("[automation is terminating]");
    }
}
