package lmi;

import lmi.Constant.ExceptionType;

// useful java.lang.Exception
//  InterruptedException
//  IllegalArgumentException
//  IndexOutOfBoundsException

public class LMIException extends RuntimeException {
    public ExceptionType type;

    public LMIException(ExceptionType type) { this.type = type; }

    public String toString() { return "{ type: " + this.type + " }"; }
}
