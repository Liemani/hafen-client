package lmi.macro;

public class Connect implements Runnable {
    public void run() {
        try {
            main();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void main() throws InterruptedException {
        String characterName = "Liemani";

        haven.Coord logInButtonLocation = centerOfLogInButton();
        lmi.api.AWTEventGenerator.generateMouseClickGeneral(logInButtonLocation, java.awt.event.MouseEvent.BUTTON1);
        waitLoadingCharacterList();
        lmi.api.WidgetMessageHandler.selectCharacter(lmi.ObjectShadow.characterList_, characterName);
    }

    private void waitLoadingCharacterList() throws InterruptedException {
        while (lmi.ObjectShadow.characterList_ == null)
            Thread.currentThread().sleep(100);
    }

//  on boot haven.RootWidget@5f832aaa

    private haven.Coord centerOfLogInButton() {
        return new haven.Coord(400, 510).add(lmi.ObjectShadow.rootWidget_.child.c);
    }
}
