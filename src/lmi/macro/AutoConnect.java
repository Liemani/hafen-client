package lmi.macro;

public class AutoConnect implements Runnable {
    public void run() {
        try {
            main();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) { e.printStackTrace(); }

        didRun();
    }

    public void main() throws InterruptedException {
        String characterName = "test";

        haven.Coord logInButtonLocation = centerOfLogInButton();
        AWTEventGenerator.generateMouseClickGeneral(logInButtonLocation, java.awt.event.MouseEvent.BUTTON1);
        waitLoadingCharacterList();
        Util.selectCharacter(characterName);
    }

    private void waitLoadingCharacterList() throws InterruptedException {
        while (lmi.ObjectShadow.characterList_ == null)
            Thread.currentThread().sleep(100);
    }

//  on boot haven.RootWidget@5f832aaa

    private haven.Coord centerOfLogInButton() {
        return new haven.Coord(400, 510).add(lmi.ObjectShadow.rootWidget_.child.c);
    }

    private void didRun() {
        System.out.println("[macro is terminating]");
    }
}
