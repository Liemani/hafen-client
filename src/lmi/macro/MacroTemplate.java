package lmi.macro;

public class MacroTemplate implements Runnable {
    public void run() {
        try {
            main();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) { e.printStackTrace(); }

        didRun();
    }

    private void main() throws InterruptedException {
        while (!Thread.interrupted()) {
            // compose your macro code here...
        }
    }

    private void didRun() {
        System.out.println("[macro is terminating]");
    }
}
