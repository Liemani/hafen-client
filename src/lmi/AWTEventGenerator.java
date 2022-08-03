package lmi;

import java.awt.event.MouseEvent;
import java.awt.event.InputEvent;

class AWTEventGenerator {
    static haven.Coord mouseLocation_ = new haven.Coord();

    static void setMouseLocation(int x, int y) {
        mouseLocation_.x = x;
        mouseLocation_.y = y;
    }

    // methods for command
    static void generateMouseClick() {
        generateMouseEvent(
                MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(),
                InputEvent.BUTTON1_MASK,
                mouseLocation_.x,
                mouseLocation_.y,
                1,
                false,
                MouseEvent.BUTTON1);
        generateMouseEvent(
                MouseEvent.MOUSE_RELEASED,
                System.currentTimeMillis(),
                InputEvent.BUTTON1_MASK,
                mouseLocation_.x,
                mouseLocation_.y,
                1,
                false,
                MouseEvent.BUTTON1);
    }

    // private methods
    private static void generateMouseEvent(int id, long when, int modifiers, int x, int y, int clickCount, boolean popupTrigger, int button) {
        MouseEvent event = new MouseEvent(
                ObjectShadow.joglPanel_,
                id,
                when,
                modifiers,
                x,
                y,
                clickCount,
                popupTrigger,
                button);

        synchronized(ObjectShadow.dispatcher_) {
            ObjectShadow.dispatcher_.events.add(event);
            ObjectShadow.dispatcher_.notifyAll();
        }
    }
}
