package lmi.api;

import lmi.*;
import lmi.collection.Array;

public class Gob {
    // Field
    final private haven.Gob gob_;

    // Constructor
    private Gob(haven.Gob gob) { gob_ = gob; }

    // Factory
    public Gob of(haven.Gob gob) { return new Gob(gob_); }

    // Access Property
    public Point location() { return Point.of(gob_.rc); }
    public double direction() { return gob_.a; }
    public double velocity() { return gob_.getv(); }

    public <C extends haven.GAttrib> C attribute(Class<C> attributeClass) {
        return gob_.getattr(attributeClass);
    }

    public haven.Resource resource() { return gob_.getres(); }

    public String resourceName() {
        final haven.Resource resource = this.resource();
        return resource != null ? resource.name : null;
    }

    public String resourceBasename() {
        final String resourceName = this.resourceName();
        if (resourceName == null) return null;

        final int lastIndexOfSlash = resourceName.lastIndexOf('/');
        if (lastIndexOfSlash < 0) return resourceName;

        final String basename = resourceName.substring(lastIndexOfSlash + 1);
        return basename;
    }

    // Instance Method
    public boolean equals(Gob gob) { return this.gob_ == gob.gob_; }
    public boolean isAt(Point point) { return this.location().equals(point); }
    public boolean isMoving() { return this.velocity() != 0.0; }
    public boolean isStop() { return !isMoving(); }
    public double distance(Point point) { return this.location().distance(point); }

    public Array<String> poseArray() {
        final haven.Composite composite = this.attribute(haven.Composite.class);
        return composite != null ? composite.poseArray() : null;
    }

    public boolean hasPose(String poseName) {
        Array<String> poseArray = this.poseArray();
        return poseArray.containsWhere(pose -> pose.endsWith(poseName));
    }

    public Gob followingTarget() {
        final haven.Moving moving = this.attribute(haven.Moving.class);
        if (moving == null) return null;
        if (!(moving instanceof haven.Following)) return null;

        final haven.Following following = (haven.Following)moving;
        final haven.Gob targetGob = following.tgt();
        return new Gob(targetGob);
    }

    public boolean isFollowing(Gob gob) { return this.followingTarget().equals(gob); }
    public boolean isLifting(Gob gob) { return gob.isFollowing(this); }




    // Statiac Method
    public static java.util.Iterator<haven.Gob> iterator() { return ObjectShadow.objectCache().iterator(); }
    public static haven.Coord2d location(haven.Gob gob) { return gob.rc; }
    public static haven.Coord locationInCoord(haven.Gob gob) { return Coordinate.toCoord(gob.rc); }
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
    public static <C extends haven.GAttrib> C attribute(haven.Gob gob, Class<C> attributeClass) {
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
                iterator = Gob.iterator();
                gob = closestGob(iterator);
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
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
            if (Gob.isAt(Self.gob(), gob))
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
        return Gob.location(lhs).dist(Gob.location(rhs));
    }

    // compare coordiante
    public static boolean isAt(haven.Gob lhs, haven.Gob rhs) {
        haven.Coord2d lhsLocation = Gob.location(lhs);
        haven.Coord2d rhsLocation = Gob.location(rhs);
        return Coordinate.equals(lhsLocation, rhsLocation);
    }

    public static boolean isAt(haven.Gob gob, haven.Coord2d point) {
        haven.Coord2d gobLocation = Gob.location(gob);
        return Coordinate.equals(gobLocation, point);
    }

    public static boolean isAt(haven.Gob gob, haven.Coord point) {
        haven.Coord2d gobLocation = Gob.location(gob);
        return Coordinate.equals(gobLocation, point);
    }

    // pose
    public static Array<String> poseArray(haven.Gob gob) {
        final haven.Composite composite = gob.getattr(haven.Composite.class);
        if (composite == null)
            return null;
        return composite.poseArray();
    }

    public static boolean hasPose(haven.Gob gob, String poseName) {
        Array<String> poseArray = Gob.poseArray(gob);
        return poseArray.containsWhere(pose -> pose.endsWith(poseName));
    }

    // etc
    public static int id(haven.Gob gob) {
        return (int)gob.id;
    }

    public static haven.Gob followingTarget(haven.Gob follower) {
        final haven.Moving moving = Gob.attribute(follower, haven.Moving.class);
        if (moving == null) return null;
        if (!(moving instanceof haven.Following)) return null;

        final haven.Following following = (haven.Following)moving;
        final haven.Gob targetGob = following.tgt();
        return targetGob;
    }

    public static boolean isGobFollowing(haven.Gob follower, haven.Gob target) {
        return Gob.followingTarget(follower) == target;
    }

    public static boolean isGobLifting(haven.Gob follower, haven.Gob target) {
        return isGobFollowing(target, follower);

    }

    // TODO implement this
    public static boolean isFollowing(haven.Gob gob, haven.Gob target) {
        return false;
    }
}
