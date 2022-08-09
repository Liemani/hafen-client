package lmi.macro;

public class Util {
    static haven.Gob gob_;

    public static void storeLastClickedGob(haven.Gob gob) { gob_ = gob; }
    public static void printLastClickedGob() { lmi.Debug.debugDescribeField(gob_); }

//      public static void autoConnect() {
//          // TODO move awt functions to lmi.macro package
//          click login
//          if (test exist)
//              selectCharacter("test");
//      }

    public static void selectCharacter(String name) {
        lmi.ObjectShadow.charList_.wdgmsg(lmi.Constant.Command.SELECT_CHARACTER, name);
    }
}
