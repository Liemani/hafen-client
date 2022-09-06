package lmi;

import lmi.Constant.ExceptionType;

// useful java.lang.Exception
//  InterruptedException
//  IllegalArgumentException
//  IndexOutOfBoundsException

public class LMIException extends RuntimeException {
    // Field
    private ExceptionType _type;

    // Constructor
    public LMIException(ExceptionType type) { _type = type; }

    // Getter;
    public ExceptionType type() { return _type; }

    public String toString() {
        return "{ type: " + _type + " }";
    }
}
