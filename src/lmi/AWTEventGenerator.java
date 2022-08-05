package lmi;

import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;

class AWTEventGenerator {
    static haven.Coord mouseLocation_ = new haven.Coord();

    static void setMouseLocation(int x, int y) {
        mouseLocation_.x = x;
        mouseLocation_.y = y;
    }

    // methods for command
    // TODO generateModifiedKeyInput from generateCtrlE()
    // TODO refine all name

    // MouseEvent.BUTTON1: left mouse button
    // MouseEvent.BUTTON3: right mouse button
    static void generateMouseClickGeneral(int button) {
        generateMouseEvent(
                MouseEvent.MOUSE_PRESSED,
                0x1 << (button + 9),
                mouseLocation_.x,
                mouseLocation_.y,
                button);
        generateMouseEvent(
                MouseEvent.MOUSE_RELEASED,
                0,
                mouseLocation_.x,
                mouseLocation_.y,
                button);
        generateMouseEvent(
                MouseEvent.MOUSE_CLICKED,
                0,
                mouseLocation_.x,
                mouseLocation_.y,
                button);
    }

    // modifiers
    //  MouseEvent.CTRL_DOWN_MASK: ctrl key modifier
    //  MouseEvent.ALT_DOWN_MASK: alt key modifier
    //  MouseEvent.SHIFT_DOWN_MASK: shift key modifier
    // buttons
    //  MouseEvent.BUTTON1: left mouse button
    //  MouseEvent.BUTTON3: right mouse button
    static void generateMouseClickModified(int modifiers, int button) {
        generateKeyEvent(KeyEvent.KEY_PRESSED, modifiers, KeyEvent.VK_CONTROL, KeyEvent.CHAR_UNDEFINED, KeyEvent.KEY_LOCATION_LEFT);
        generateMouseEvent(
                MouseEvent.MOUSE_PRESSED,
                modifiers | 0x1 << (button + 9),
                mouseLocation_.x,
                mouseLocation_.y,
                button);
        generateMouseEvent(
                MouseEvent.MOUSE_RELEASED,
                modifiers,
                mouseLocation_.x,
                mouseLocation_.y,
                button);
        generateMouseEvent(
                MouseEvent.MOUSE_CLICKED,
                modifiers,
                mouseLocation_.x,
                mouseLocation_.y,
                button);
        generateKeyEvent(KeyEvent.KEY_RELEASED, 0, KeyEvent.VK_CONTROL, KeyEvent.CHAR_UNDEFINED, KeyEvent.KEY_LOCATION_LEFT);
    }

    static void generateKeyPushUpGeneralKey(int keyCode) {
        generateKeyEvent(
                KeyEvent.KEY_PRESSED,
                0,
                keyCode,
                (char)(keyCode + 32),
                KeyEvent.KEY_LOCATION_STANDARD);
        generateKeyEvent(
                KeyEvent.KEY_TYPED,
                0,
                KeyEvent.VK_UNDEFINED,
                (char)(keyCode + 32),
                KeyEvent.KEY_LOCATION_UNKNOWN);
        generateKeyEvent(
                KeyEvent.KEY_RELEASED,
                0,
                keyCode,
                (char)(keyCode + 32),
                KeyEvent.KEY_LOCATION_STANDARD);
    }

    static void generateKeyPushUpSpecialKey(int keyCode) {
        generateKeyEvent(
                KeyEvent.KEY_PRESSED,
                0,
                keyCode,
                (char)keyCode,
                KeyEvent.KEY_LOCATION_STANDARD);
        generateKeyEvent(
                KeyEvent.KEY_TYPED,
                0,
                KeyEvent.VK_UNDEFINED,
                (char)keyCode,
                KeyEvent.KEY_LOCATION_UNKNOWN);
        generateKeyEvent(
                KeyEvent.KEY_RELEASED,
                0,
                keyCode,
                (char)keyCode,
                KeyEvent.KEY_LOCATION_STANDARD);
    }

    static void generateCtrlE() {
        generateKeyEvent(
                KeyEvent.KEY_PRESSED,
                KeyEvent.CTRL_DOWN_MASK,
                KeyEvent.VK_CONTROL,
                KeyEvent.CHAR_UNDEFINED,
                KeyEvent.KEY_LOCATION_LEFT);
        generateKeyEvent(
                KeyEvent.KEY_PRESSED,
                KeyEvent.CTRL_DOWN_MASK,
                KeyEvent.VK_E,
                '\005',
                KeyEvent.KEY_LOCATION_STANDARD);
        generateKeyEvent(
                KeyEvent.KEY_TYPED,
                KeyEvent.CTRL_DOWN_MASK,
                KeyEvent.VK_UNDEFINED,
                '\005',
                KeyEvent.KEY_LOCATION_UNKNOWN);
        generateKeyEvent(
                KeyEvent.KEY_RELEASED,
                KeyEvent.CTRL_DOWN_MASK,
                KeyEvent.VK_E,
                '\005',
                KeyEvent.KEY_LOCATION_STANDARD);
        generateKeyEvent(
                KeyEvent.KEY_RELEASED,
                0,
                KeyEvent.VK_CONTROL,
                KeyEvent.CHAR_UNDEFINED,
                KeyEvent.KEY_LOCATION_LEFT);
    }

    // private methods
    private static void generateKeyEvent(int id, int modifiers, int keyCode, char keyChar, int keyLocation) {
        KeyEvent event = new KeyEvent(
                ObjectShadow.joglPanel_,
                id,
                System.currentTimeMillis(),
                modifiers,
                keyCode,
                keyChar,
                keyLocation);

        synchronized(ObjectShadow.dispatcher_) {
            ObjectShadow.dispatcher_.events.add(event);
            ObjectShadow.dispatcher_.notifyAll();
        }
    }

    private static void generateMouseEvent(int id, int modifiers, int x, int y, int button) {
        MouseEvent event = new MouseEvent(
                ObjectShadow.joglPanel_,
                id,
                System.currentTimeMillis(),
                modifiers,
                x,
                y,
                1,
                false,
                button);

        synchronized(ObjectShadow.dispatcher_) {
            ObjectShadow.dispatcher_.events.add(event);
            ObjectShadow.dispatcher_.notifyAll();
        }
    }
}
