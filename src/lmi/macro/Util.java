package lmi.macro;

public class Util {
    static haven.Gob gob_;

    public static void storeLastClickedGob(haven.Gob gob) { gob_ = gob; }
    public static void printLastClickedGob() { lmi.Debug.debugDescribeField(gob_); }

    public static void selectCharacter(String name) {
        lmi.ObjectShadow.characterList_.wdgmsg(lmi.Constant.Command.SELECT_CHARACTER, name);
    }
}
