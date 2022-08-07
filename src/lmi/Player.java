package lmi;

class Player {
    static haven.Gob gob_ = null;

    static void init(haven.Gob gob) {
        setGob(gob);
    }

    static void setGob(haven.Gob gob) {
        gob_ = gob;
    }

    // get methods
    static haven.Coord2d getCoord2d() {
        return gob_.rc;
    }

    static haven.Skeleton.Pose getPose() {
         return gob_.getpose();
    }

//      public static void saveGob(haven.ClickData inf) {
//          if (inf == null)
//              return;
//  
//          if(inf.ci instanceof haven.Composited.CompositeClick) {
//              gob = ((haven.Composited.CompositeClick) inf.ci).gi.gob;
//          } else if(inf.ci instanceof haven.Gob.GobClick) {
//              gob = ((haven.Gob.GobClick) inf.ci).gob;
//          }
//      }
}
