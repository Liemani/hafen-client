package lmi;

import java.io.PrintStream;

import java.util.Set;
import java.util.concurrent.Callable;

public class MainThread implements Runnable {
    // fields
    haven.MainFrame mainFrame_;
    haven.UIPanel uiPanel_;

    public MainThread(haven.MainFrame mainFrame, haven.UIPanel uiPanel) {
        mainFrame_ = mainFrame;
        uiPanel_ = uiPanel;
    }

    // Runnable requirment
    public void run() {
        System.out.println("[top of run()]");

        this.init();

        while (true) {
            MainThread.printCommandStringList(System.out);
            System.out.print("Enter command(\"exit\" to terminate): ");
            String commandString = lmi.Scanner.nextLine();

            if (commandString.contentEquals("exit"))
                break;

            Callable<Void> command = lmi.Command.getCommandByString(commandString);
            if (command != null) {
                try { command.call(); } catch (Exception e) {}
            }
            else {
                System.out.println("Unknown command: [" + commandString + "]");
            }

            System.out.println();
        }
    }

    // private class methods
    private static void printCommandStringList(PrintStream outputStream) {
        outputStream.println("command list:");
        Set<String> commandStringSet = lmi.Command.getCommandStringSet();
        commandStringSet.forEach((commandString) -> {
            outputStream.println("\t" + commandString);
        });
    }

    // private instance methods
    private void init() {
        lmi.Scanner.init(System.in);
        lmi.Command.init(this);
    }

    // main()
    public static void main(String args[]) {
        lmi.MainThread mainThread = new lmi.MainThread(new haven.MainFrame(null), new haven.JOGLPanel(null));
        new Thread(mainThread).start();
//      new Thread(new lmi.MainThread(this, this.p)).start();
    }
}
