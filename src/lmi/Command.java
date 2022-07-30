package lmi;

import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;

class Command {
    // type define
    private static class CommandMap extends TreeMap<String, Callable<Void>> {};

    // fields
    private static lmi.MainThread mainThread_;
    private static lmi.Command.CommandMap map_;

    // commands
    private static Void hello() {
        System.out.println("Hello, world!");

        return null;
    }

    private static Void printUIPanelClassName() {
        System.out.println("class name of uiPanel: " + mainThread_.uiPanel_.getClass().getName());

        return null;
    }

    private static Void printListOfMainFrame() {
        mainThread_.mainFrame_.list(System.out, 4);

        return null;
    }

    // package method
    static void init(lmi.MainThread mainThread) {
        mainThread_ = mainThread;

        map_ = new CommandMap();
        map_.put("hello", Command::hello);
        map_.put("printUIPanelClassName", Command::printUIPanelClassName);
    }

    static Callable<Void> getCommandByString(String commandString) {
        return map_.get(commandString);
    }

    static Set<String> getCommandStringSet() {
        return map_.keySet();
    }
}
