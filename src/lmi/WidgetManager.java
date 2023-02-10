package lmi;

import haven.*;

import static lmi.Constant.*;
import static lmi.Constant.TimeOut.*;
import static lmi.Constant.ExceptionType.*;

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
    static Window window() { return WidgetManager.gameUI().getChildOf(Window.class); }
    static Button button() { return WidgetManager.window().getChildOf(Button.class); }
    static ISBox isbox() { return WidgetManager.window().getChildOf(ISBox.class); }
    static Inventory inventory() { return WidgetManager.window().getChildOf(Inventory.class); }
    static GItem gitem() { return WidgetManager.inventory().getChildOf(GItem.class); }
    static WItem witem() { return WidgetManager.inventory().getChildOf(WItem.class); }
}
