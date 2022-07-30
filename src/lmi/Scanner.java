package lmi;

import java.io.InputStream;

public class Scanner {
    // fields
    private static java.util.Scanner scanner_;

    // public methods
    public static String nextLine() {
        return scanner_.nextLine();
    }

    // package methods
    static void init(InputStream inputStream) {
        scanner_ = new java.util.Scanner(inputStream);
    }
}
