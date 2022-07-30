package lmi;

import java.util.TreeMap;
import java.util.concurrent.Callable;

public class Command {
    // type define
    private static class CommandMap extends TreeMap<String, Callable<Void>> {};

    // class fields
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

    // public methods
    public static void execute(String command) {
        try {
            map_.get(command).call();
        } catch (Exception e) {}
    }
}
