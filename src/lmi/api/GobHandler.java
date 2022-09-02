package lmi.api;

import lmi.*;
import lmi.collection.Array;

public class GobHandler {
//      iter = oc.iterator();
//      while(iter.hasNext()) {
//          System.out.println(iter.next());
//      }
    public static java.util.Iterator<haven.Gob> iterator() { return ObjectShadow.objectCache().iterator(); }
    public static haven.Coord2d location(haven.Gob gob) { return gob.rc; }
    public static haven.Coord locationInCoord(haven.Gob gob) { return CoordinateHandler.convertCoord2dToCoord(gob.rc); }
    public static boolean isStop(haven.Gob gob) { return velocity(gob) == 0.0; }
    public static boolean isMoving(haven.Gob gob) { return velocity(gob) != 0.0; }

    // resource
    public static haven.Resource resource(haven.Gob gob) {
        if (gob == null)
            return null;
        return gob.getres();
    }

    public static String resourceName(haven.Gob gob) {
        haven.Resource resource = resource(gob);
        if (resource == null)
            return null;
        return resource.name;
    }

    public static String resourceBasename(haven.Gob gob) {
        String name = resourceName(gob);
        int lastIndexOfSlash = name.lastIndexOf('/');
        if (lastIndexOfSlash < 0)
            return name;
        String basename = name.substring(lastIndexOfSlash + 1);
        return basename;
    }

    // attribute
    public static haven.GAttrib attribute(haven.Gob gob, Class<? extends haven.GAttrib> attributeClass) {
        if (gob == null)
            return null;
        return gob.getattr(attributeClass);
    }

    // velocity
    public static double velocity(haven.Gob gob) { return gob.getv(); }

    // find gob
    public static haven.Gob closestGob() {
        java.util.Iterator<haven.Gob> iterator;
        haven.Gob gob = null;
        while (true) {
            try {
                iterator = GobHandler.iterator();
                gob = closestGob(iterator);
                break;
            } catch (Exception e) {}
        }

        return gob;
    }

    private static haven.Gob closestGob(java.util.Iterator<haven.Gob> iterator) {
        haven.Gob gob;
        double distance;

        haven.Gob closestGob = null;
        double closestDistance = 1100.0;

        while (iterator.hasNext()) {
            gob = iterator.next();
            if (gob.getClass() != haven.Gob.class)
                continue;
            if (GobHandler.isAt(Self.gob(), gob))
                continue;
            distance = Self.distance(gob);
            if (distance < closestDistance) {
                closestGob = gob;
                closestDistance = distance;
            }
        }

        return closestGob;
    }

    public static double distance(haven.Gob lhs, haven.Gob rhs) {
        return GobHandler.location(lhs).dist(GobHandler.location(rhs));
    }

    // compare coordiante
    public static boolean isAt(haven.Gob lhs, haven.Gob rhs) {
        haven.Coord2d lhsLocation = GobHandler.location(lhs);
        haven.Coord2d rhsLocation = GobHandler.location(rhs);
        return CoordinateHandler.equals(lhsLocation, rhsLocation);
    }

    public static boolean isAt(haven.Gob gob, haven.Coord2d point) {
        haven.Coord2d gobLocation = GobHandler.location(gob);
        return CoordinateHandler.equals(gobLocation, point);
    }

    public static boolean isAt(haven.Gob gob, haven.Coord point) {
        haven.Coord2d gobLocation = GobHandler.location(gob);
        return CoordinateHandler.equals(gobLocation, point);
    }

    // etc
    public static int id(haven.Gob gob) {
        return (int)gob.id;
    }

    public static Array<String> poseArray(haven.Gob gob) {
        haven.Composite composite = gob.getattr(haven.Composite.class);
        if (composite == null)
            return null;
        return composite.poseArray();
    }

    public static boolean hasPose(haven.Gob gob, String poseName) {
        Array<String> poseArray = GobHandler.poseArray(gob);
        return poseArray.containsWhere(pose -> pose.endsWith(poseName));
    }

    // TODO implement this
    public static boolean isFollowing(haven.Gob gob, haven.Gob target) {
        return false;
    }
}
