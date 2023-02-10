package lmi;

class ErrorMessageManager {
    private static String _message;

    static String message() { return _message; }
    static void setMessage(String message) { _message = message; }

    static void clear() { _message = null; }
}
