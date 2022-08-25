package lmi;

import lmi.api.*;

public class Delegate {
    public static void poseDidChange(haven.Gob gob) {
        // Composite::poseDidChange()
    }

    public static void linMoveDidDelete(haven.Gob gob) {
        notifySelfIfArrived(gob);
    }

    public static void didMove(haven.Gob gob) {
        notifySelfIfArrived(gob);
    }

    // private methods
    private static void notifySelfIfArrived(haven.Gob gob) {
        if (gob == Self.gob()) {
            Self.notifyIfArrived(gob);
        }
    }
}
