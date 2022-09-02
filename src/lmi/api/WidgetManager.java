package lmi.api;

import lmi.*;

public class WidgetManager {
    public static haven.GItem cursorGItem() {
        haven.Widget child = ObjectShadow.gameUI().child;
        while (child != null) {
            if (child instanceof haven.GItem)
                return (haven.GItem)child;
            child = child.next;
        }

        return null;
    }

    public static haven.MenuGrid menuGrid() {
        return ObjectShadow.gameUI().menu;
    }

    public static haven.Indir<haven.Resource> cursor() {
        return ObjectShadow.rootWidget().cursor;
    }
}
