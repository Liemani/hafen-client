package lmi.macro;

public class Test implements Runnable {
    public void run() {
        try {
            main();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void main() throws InterruptedException {
        int index = 0;
        while (!Thread.interrupted()) {
            if (index % 100000000 == 0) {
                if (Thread.interrupted())
                    System.out.println("1");
                else
                    System.out.println("2");
            }
            ++index;
        }
    }
}
