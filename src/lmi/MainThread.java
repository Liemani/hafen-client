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
        System.out.println("[lmi.MainThread::run()]");

        init();

        while (true) {
            System.out.println("===================================");
            String commandString = lmi.Scanner.nextLineWithPrompt("enter command(exit to terminate)");

            if (commandString.contentEquals("exit")) {
                break;
            } else if (commandString.contentEquals("")
                    || commandString.contentEquals("h")
                    || commandString.contentEquals("help")) {
                printCommandStringList(System.out);
                continue;
            }

            Method command = lmi.Command.getCommandByString(commandString);
            if (command != null) {
                System.out.println("[" + command.getName() + ": invoking]");
                try { command.invoke(null); } catch (Exception e) { e.printStackTrace(); }
            } else {
                System.out.println("[" + commandString + ": unknown command]");
            }
        }

        System.out.println("[terminating]");
        ObjectShadow.interruptMainThread();
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
        Scanner.init(System.in);
        Command.init();
        ObjectFinder.init();
    }

    // main()
    public static void main(String[] args) {
        MainThread mainThread = new MainThread(new haven.MainFrame(null), new haven.JOGLPanel(null));
        new Thread(mainThread).start();
//      new Thread(new MainThread(this, this.p)).start();
    }
}
