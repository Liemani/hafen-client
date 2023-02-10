package lmi;

import lmi.Constant.ExceptionType;

public class LMIIntException extends LMIException {
    public value;

    public LMIIntException(ExceptionType type, int value) {
        this.type = type;
        this.value = value;
    }

    public String toString() { return "{ type: " + this.type + ", value: " + this.value + " }"; }
}
