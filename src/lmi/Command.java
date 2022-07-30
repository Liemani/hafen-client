package lmi;

import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;

class Command {
    // type define
    private static class CommandMap extends TreeMap<String, Callable<Void>> {};

    // fields
    private static CommandMap map_ = Command.newClassFieldMap_();
    private static CommandMap newClassFieldMap_() {
        CommandMap map = new CommandMap();
        map.put("hello", Command::hello);

        return map;
    }

    // commands
    private static Void hello() {
        System.out.println("Hello, world!");

        return null;
    }

    // package method
    static Callable<Void> getCommandByString(String commandString) {
        return map_.get(commandString);
    }

    static Set<String> getCommandStringSet() {
        return map_.keySet();
    }
}
