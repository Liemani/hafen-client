package lmi;

import java.io.PrintStream;

import java.util.Set;

import java.lang.reflect.Method;

public class MainThread implements Runnable {
    // constructor
    public MainThread(Object ... args) {
        ObjectShadow.init(args);
    }

    // Runnable requirment
    public void run() {
        System.out.println("[top of run()]");

        this.init();

        while (true) {
            MainThread.printCommandStringList(System.out);
            System.out.println("-----------------------------------");
            System.out.print("Enter command(\"exit\" to terminate): ");
            String commandString = lmi.Scanner.nextLine();

            if (commandString.contentEquals("exit"))
                break;

            Method command = lmi.Command.getCommandByString(commandString);
            if (command != null) {
                try { command.invoke(null); } catch (Exception e) { System.out.println("command invoke failed"); }
            }
            else {
                System.out.println("[" + commandString + ": unknown command]");
            }
            System.out.println("===================================");
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
        lmi.Command.init();
    }

    // main()
    public static void main(String[] args) {
        lmi.MainThread mainThread = new lmi.MainThread(new haven.MainFrame(null), new haven.JOGLPanel(null));
        new Thread(mainThread).start();
//      new Thread(new lmi.MainThread(this, this.p)).start();
    }
}
