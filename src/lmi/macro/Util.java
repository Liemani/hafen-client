package lmi.macro;

//  좌표 체계는 double 값으로 11의 배수가 각 타일의 한 꼭지점이다
//  int로는 한 타일은 1024의 간격을 갖는다

public class Util {
    static haven.Gob gob_;

    public static void storeLastClickedGob(haven.Gob gob) { gob_ = gob; }
    public static haven.Gob lastClickedGob() { return gob_; }
    public static void describeLastClickedGob() {
        lmi.Debug.debugDescribeField(gob_);
    }

    //  public static void waitArriving() {
        //  while () {
            //  Thread.sleep(100);
        //  }
        //  while () {
            //  Thread.sleep(100);
        //  }
        //  Self.
    //  }

    public static void waitHourGlassFailable() throws InterruptedException {
        while (lmi.ObjectShadow.gameUI_.prog != null) {
            Thread.sleep(100);
        }
    }

    public static void selectCharacter(String name) {
        lmi.ObjectShadow.characterList_.wdgmsg(lmi.Constant.Command.SELECT_CHARACTER, name);
    }

    public static haven.Coord convertCoord2dToCoord(haven.Coord2d point) {
        return new haven.Coord(point.floor(haven.OCache.posres));
    }
}
