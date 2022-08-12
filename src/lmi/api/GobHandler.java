package lmi.api;

public class GobHandler {
    private static haven.OCache objectCache_;

    public static void init(haven.OCache objectCache) { objectCache_ = objectCache; }

    //  while(it.hasNext()) {
    //      System.out.println(it.next());
    //  }
    public static java.util.Iterator<haven.Gob> iterator() { return objectCache_.iterator(); }

    public static haven.Coord2d location(haven.Gob gob) {
        return gob.rc;
    }

    public static boolean isStop(haven.Gob gob) {
        return velocity(gob) == 0.0;
    }

    public static boolean isMoving(haven.Gob gob) {
        return velocity(gob) != 0.0;
    }

    public static haven.Resource resource(haven.Gob gob) {
        return gob.getres();
    }

    public static double velocity(haven.Gob gob) {
        return gob.getv();
    }
}
