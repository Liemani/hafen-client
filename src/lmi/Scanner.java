package lmi;

import java.io.InputStream;

public class Scanner {
    // fields
    private static java.util.Scanner _scanner;

    // public methods
    public static String nextLine() {
        return _scanner.nextLine();
    }

    public static String nextLineWithPrompt(String prompt) {
        System.out.print(prompt + " % ");
        return _scanner.nextLine();
    }

    // package methods
    static void init(InputStream inputStream) {
        _scanner = new java.util.Scanner(inputStream);
    }
}
