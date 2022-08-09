package lmi;

public class Initializer {
    public static void init() {
        Scanner.init(System.in);
        Command.init();
        ObjectFinder.init();
        ObjectShadow.init();
        new Thread(new lmi.MainRunnable()).start();
    }

    public static void initMainFrame(haven.MainFrame mainFrame) { ObjectShadow.initMainFrame(mainFrame); }
    public static void initMainThread(Thread mainThread) { ObjectShadow.initMainThread(mainThread); }
    public static void initJOGLPanel(haven.JOGLPanel joglPanel) { ObjectShadow.initJOGLPanel(joglPanel); }
    public static void initDispatcher(haven.UIPanel.Dispatcher dispatcher) { ObjectShadow.initDispatcher(dispatcher); }
    public static void initUIRunner(haven.UI.Runner uiRunner) { ObjectShadow.initUIRunner(uiRunner); }
    public static void initUI(haven.UI ui) { ObjectShadow.initUI(ui); }
    public static void initRootWidget(haven.RootWidget rootWidget) { ObjectShadow.initRootWidget(rootWidget); }
    public static void initGameUI(haven.GameUI gameUI) { ObjectShadow.initGameUI(gameUI); }
    public static void initMapView(haven.MapView mapView) { ObjectShadow.initMapView(mapView); }
    public static void initSession(haven.Session session) { ObjectShadow.initSession(session); }
    public static void initCharList(haven.Charlist characterList) { ObjectShadow.initCharacterList(characterList); }

    public static void setGaugeArray(haven.IMeter gauge) { ObjectShadow.setGaugeArray(gauge); }
}
