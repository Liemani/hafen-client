package lmi.macro;

import lmi.api.*;

public class Connect implements Runnable {
    public void run() {
        try {
            main();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void main() throws InterruptedException {
        String characterName = "Riedar";

        haven.Coord logInButtonLocation = centerOfLogInButton();
        lmi.api.AWTEventGenerator.generateMouseClickGeneral(logInButtonLocation, java.awt.event.MouseEvent.BUTTON1);
        waitLoadingCharacterList();
        WidgetMessageHandler.selectCharacter(lmi.ObjectShadow.characterList(), characterName);
    }

    private void waitLoadingCharacterList() throws InterruptedException {
        while (lmi.ObjectShadow.characterList() == null)
            Thread.currentThread().sleep(100);
    }

//  on boot haven.RootWidget@5f832aaa

    private haven.Coord centerOfLogInButton() {
        // TODO chang lmi.ObjectShadow.rootWidget() to really intend
        return new haven.Coord(400, 510).add(lmi.ObjectShadow.rootWidget().child.c);
    }
}
