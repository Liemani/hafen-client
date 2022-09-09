package lmi;

import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;

public class AWTEventGenerator {
    public static haven.Coord _mouseLocation = new haven.Coord();

    public static void setMouseLocation(int x, int y) {
        _mouseLocation.x = x;
        _mouseLocation.y = y;
    }

    public static void printRecordedMouseLocation() {
        Debug.describeField(_mouseLocation);
    }

    // methods for command
    // TODO generateModifiedKeyInput from generateCtrlE()
    // TODO refine all name

    // MouseEvent.BUTTON1: left mouse button
    // MouseEvent.BUTTON3: right mouse button
    public static void generateMouseClickGeneral(int button) {
        generateMouseClickGeneral(_mouseLocation, button);
    }

    public static void generateMouseClickGeneral(haven.Coord location, int button) {
        generateMouseEvent(
                MouseEvent.MOUSE_PRESSED,
                0x1 << (button + 9),
                location.x,
                location.y,
                button);
        generateMouseEvent(
                MouseEvent.MOUSE_RELEASED,
                0,
                location.x,
                location.y,
                button);
        generateMouseEvent(
                MouseEvent.MOUSE_CLICKED,
                0,
                location.x,
                location.y,
                button);
    }

    // modifiers
    //  MouseEvent.CTRL_DOWN_MASK: ctrl key modifier
    //  MouseEvent.ALT_DOWN_MASK: alt key modifier
    //  MouseEvent.SHIFT_DOWN_MASK: shift key modifier
    // buttons
    //  MouseEvent.BUTTON1: left mouse button
    //  MouseEvent.BUTTON3: right mouse button
    public static void generateMouseClickModified(int modifiers, int button) {
        generateKeyEvent(KeyEvent.KEY_PRESSED, modifiers, KeyEvent.VK_CONTROL, KeyEvent.CHAR_UNDEFINED, KeyEvent.KEY_LOCATION_LEFT);
        generateMouseEvent(
                MouseEvent.MOUSE_PRESSED,
                modifiers | 0x1 << (button + 9),
                _mouseLocation.x,
                _mouseLocation.y,
                button);
        generateMouseEvent(
                MouseEvent.MOUSE_RELEASED,
                modifiers,
                _mouseLocation.x,
                _mouseLocation.y,
                button);
        generateMouseEvent(
                MouseEvent.MOUSE_CLICKED,
                modifiers,
                _mouseLocation.x,
                _mouseLocation.y,
                button);
        generateKeyEvent(KeyEvent.KEY_RELEASED, 0, KeyEvent.VK_CONTROL, KeyEvent.CHAR_UNDEFINED, KeyEvent.KEY_LOCATION_LEFT);
    }

    public static void generateKeyPushUpGeneralKey(int keyCode) {
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

    public static void generateKeyPushUpSpecialKey(int keyCode) {
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

    public static void generateCtrlE() {
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
                ObjectShadow.joglPanel(),
                id,
                System.currentTimeMillis(),
                modifiers,
                keyCode,
                keyChar,
                keyLocation);

        synchronized(ObjectShadow.dispatcher()) {
            ObjectShadow.dispatcher().events.add(event);
            ObjectShadow.dispatcher().notifyAll();
        }
    }

    private static void generateMouseEvent(int id, int modifiers, int x, int y, int button) {
        MouseEvent event = new MouseEvent(
                ObjectShadow.joglPanel(),
                id,
                System.currentTimeMillis(),
                modifiers,
                x,
                y,
                1,
                false,
                button);

        synchronized(ObjectShadow.dispatcher()) {
            ObjectShadow.dispatcher().events.add(event);
            ObjectShadow.dispatcher().notifyAll();
        }
    }

    // etc
    static Void openAxeCraftWindow() {
        AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_C);
        AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_C);
        AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_T);
        AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_A);
        AWTEventGenerator.generateKeyPushUpGeneralKey(java.awt.event.KeyEvent.VK_S);
        return null;
    }

    static Void typeEnter() {
        AWTEventGenerator.generateKeyPushUpSpecialKey(java.awt.event.KeyEvent.VK_ENTER);
        return null;
    }

    static Void typeTab() {
        AWTEventGenerator.generateKeyPushUpSpecialKey(java.awt.event.KeyEvent.VK_TAB);
        return null;
    }

    static boolean isESC(java.awt.event.KeyEvent keyEvent) {
        return (keyEvent.getModifiers() == 0
            && keyEvent.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE);
    }
}
