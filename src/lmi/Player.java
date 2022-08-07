package lmi;

class Player {
    // never reuse return value(it could be changed)
    static haven.Gob gob() {
        if (ObjectShadow.mapView_ == null)
            return null;
        else
            return ObjectShadow.mapView_.player();
    }

    // TODO test new methods
    // methods
    static haven.Coord2d location() {
        return gob().rc;
    }

    static haven.Skeleton.Pose getPose() {
         return gob().getpose();
    }

    // do action
    static void dig() {
        doAct("dig");
    }

    // TODO add more action after test dig()
    // dig, mine, carry, destroy, fish, inspect, repair, crime, swim, tracking, aggro, shoot
    static void doAct(String action) {
        ObjectShadow.gameUI_.menu.wdgmsg("act", action);
    }
}
