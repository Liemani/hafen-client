package lmi;

public class Run implements Runnable {
    public void run() {
        System.out.println("[top of run()]");

//          String line = getLine();
        lmi.Command.execute("hello");
    }

    public static void main(String args[]) {
        new Thread(new lmi.Run()).start();
    }
}
