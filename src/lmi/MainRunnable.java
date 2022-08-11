package lmi;

public class MainRunnable implements Runnable {
    // Runnable requirment
    public void run() {
        System.out.println("[lmi.MainRunnable::run()]");

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

            java.lang.reflect.Method command = lmi.Command.getCommandByString(commandString);
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
    private static void printCommandStringList(java.io.PrintStream outputStream) {
        outputStream.println("command list:");
        java.util.Set<String> commandStringSet = lmi.Command.getCommandStringSet();
        commandStringSet.forEach((commandString) -> {
            outputStream.println("\t" + commandString);
        });
    }

    // main()
    public static void main(String[] args) {
//          MainRunnable mainRunnable = new MainRunnable(new haven.MainFrame(null), new haven.JOGLPanel(null));
//          new Thread(mainRunnable).start();
    }
}
