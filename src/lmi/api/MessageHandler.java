package lmi.api;

import lmi.*;

public class MessageHandler {
    public static String getAction(haven.RMessage message) {
        return lmi.Util.convertToString(message.wbuf, 4);
    }
}
