package lmi.api;

import lmi.*;

public class MessageHandler {
    public static String getCommand(haven.RMessage message) {
        return lmi.Util.convertToString(message.wbuf, 4);
    }
}
