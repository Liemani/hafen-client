package lmi;

import haven.*;

class WidgetManager {
    static GItem cursorGItem() {
        Widget child = ObjectShadow.gameUI().child;
        while (child != null) {
            if (child instanceof GItem)
                return (GItem)child;
            child = child.next;
        }

        return null;
    }

    static MenuGrid menuGrid() {
        return ObjectShadow.gameUI().menu;
    }

    static Indir<Resource> cursor() {
        return ObjectShadow.rootWidget().cursor;
    }

    static GameUI gameUI() { return (GameUI)ObjectShadow.rootWidget().child; }
    static ChatUI chatUI() { return WidgetManager.gameUI().getChildOf(ChatUI.class); }
}
