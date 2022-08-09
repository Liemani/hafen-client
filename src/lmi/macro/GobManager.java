package lmi.macro;

public class GobManager {
    private static haven.OCache objectCache_;

    static void init(haven.OCache objectCache) { objectCache_ = objectCache; }

    //  while(it.hasNext()) {
        //  System.out.println(it.next());
    //  }
    static java.util.Iterator<haven.Gob> iterator() { return objectCache_.iterator(); }

    static haven.Coord2d location(haven.Gob gob) {
        return gob.rc;
    }

    static boolean isStop(haven.Gob gob) {
        return velocity(gob) == 0.0;
    }

    static boolean isMoving(haven.Gob gob) {
        return velocity(gob) != 0.0;
    }

    static haven.Resource resource(haven.Gob gob) {
        return gob.getres();
    }

    static double velocity(haven.Gob gob) {
        return gob.getv();
    }
}
