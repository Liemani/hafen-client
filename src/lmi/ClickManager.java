package lmi;

import haven.Gob;
import haven.Coord;

import lmi.Constant.Message;
import lmi.Constant.Signal;
import static lmi.Constant.ExceptionType.*;
import static lmi.Constant.Signal.*;

class ClickManager {
    // Field
    private static haven.ClickData _clickData;
    private static Rect _selectedArea;

    // Getter Setter
    static void setClickData(haven.ClickData clickData) { _clickData = clickData; }
    static void setSelectedArea(Rect area) { _selectedArea = area; }

    // Public Method
    static Gob getGob() {
        WaitManager.waitSignal(S_OBJECT_DID_CLICKED);

        final haven.Clickable clickable = _clickData.ci;
        Gob clickedGob = null;
        if (clickable.getClass() == Gob.GobClick.class) {
            final Gob.GobClick gobClick = (Gob.GobClick)clickable;
            clickedGob = gobClick.gob;
        } else if (clickable.getClass() == haven.Composited.CompositeClick.class) {
            final haven.Composited.CompositeClick compositeClick = (haven.Composited.CompositeClick)clickable;
            final Gob.GobClick gobClick = compositeClick.gi;
            clickedGob = gobClick.gob;
        }

        return clickedGob;
    }

    static Rect getArea() {
        ObjectShadow.mapView().newSelector();
        WaitManager.waitSignal(S_AREA_DID_SELECTED);
        ObjectShadow.mapView().destroySelector();

        return _selectedArea;
    }
}
