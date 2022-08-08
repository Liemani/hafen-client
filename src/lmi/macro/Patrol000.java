package lmi.macro;

import lmi.collection.Array;

public class Patrol000 implements Runnable {
    private Array<haven.Coord2d> path_;

    public void run() {
        setPath();

        try {
            while (!Thread.interrupted()) {
                for (haven.Coord2d location : path_) {
                    Player.mapViewClick(location.x, location.y, 1, 0);
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) { e.printStackTrace(); }

        System.out.println("[macro is terminated]");
    }

    private void setPath() {
        if (path_ == null)
            path_ = new Array<haven.Coord2d>();
        path_.removeAll();
        path_.append(Player.location());
        path_.append(Player.location().add(100, 100));
    }
}
