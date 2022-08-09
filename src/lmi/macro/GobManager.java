package lmi.macro;

public class GobManager {
    private static haven.OCache objectCache_;

    static void init(haven.OCache objectCache) { objectCache_ = objectCache; }

    //  while(it.hasNext()) {
        //  System.out.println(it.next());
    //  }
    static java.util.Iterator<haven.Gob> iterator() { return objectCache_.iterator(); }
}
