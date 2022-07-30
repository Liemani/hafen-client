package lmi;

import java.io.PrintStream;

import java.util.Set;
import java.util.concurrent.Callable;

public class MainThread implements Runnable {
    // Runnable requirment
    public void run() {
        System.out.println("[top of run()]");

        initThread();

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

    // private methods
    private static void initThread() {
        Scanner.initScanner(System.in);
    }

    // private methods
    private static void printCommandStringList(PrintStream outputStream) {
        outputStream.println("command list:");
        Set<String> commandStringSet = lmi.Command.getCommandStringSet();
        commandStringSet.forEach((commandString) -> {
            outputStream.println("\t" + commandString);
        });
    }

    // main()
    public static void main(String args[]) {
        new Thread(new lmi.MainThread()).start();
    }
}
