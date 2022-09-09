package lmi;

import haven.*;

import static lmi.Constant.ExceptionType.*;

public class MainCommand implements haven.Console.Command {
    // haven.Console.Command Requirement
	public void run(Console cons, String[] args) throws Exception {
        new Thread(new MainRunnable(args)).start();
    }

    private static class MainRunnable implements Runnable {
        private String[] _args;
        private MainRunnable(String[] args) { _args = args; }

        // Runnable Requirment
        public void run() {
            String commandString = "";
            try {
                _checkArgument();

                commandString = _args[1];
                if (commandString.contentEquals("help"))
                    throw new LMIException(ET_COMMAND_HELP);

                _invokeCommand(commandString);
            } catch (Exception e) {
                if (e instanceof LMIException) {
                    final LMIException lmiException = (LMIException)e;
                    if (lmiException.type() == ET_COMMAND_HELP)
                        _printHelp(ObjectShadow.ui().cons.out);
                    else if (lmiException.type() == ET_COMMAND_MATCH)
                        Util.printErrorMessage("[" + commandString + "]가 뭔지 모르겠어요");
                } else {
                    Util.debugPrint(e);
                    e.printStackTrace();
                }
            }
        }

        private void _checkArgument() {
            if (_args.length != 2) throw new LMIException(ET_COMMAND_HELP);
        }

        private void _invokeCommand(String commandString) throws Exception {
            java.lang.reflect.Method command = lmi.Command.getCommandByString(commandString);
            if (command != null)
                command.invoke(null);
            else
                throw new LMIException(ET_COMMAND_MATCH);
        }

        // Help
        private static void _printHelp(java.io.PrintWriter writer) {
            writer.println("usage: lmi <command>");
            writer.println(" ");
            _printCommandStringList(writer);
        }

        private static void _printCommandStringList(java.io.PrintWriter writer) {
            writer.println("Command List:");
            java.util.Set<String> commandStringSet = lmi.Command.getCommandStringSet();
            commandStringSet.forEach((commandString) -> {
                    writer.println("\t" + commandString);
                    });
        }
    }
}
