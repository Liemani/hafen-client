package lmi;

public class Initializer {
    public static void init() {
        Scanner.init(System.in);
        Command.init();
        ObjectFinder.init();
        ObjectShadow.init();
        new Thread(new lmi.MainRunnable()).start();
        Debug.init();
    }

    // set lmi.api.ObjectShadow
    public static void initMainFrame(haven.MainFrame mainFrame) { ObjectShadow.setMainFrame(mainFrame); }
    public static void initMainThread(Thread mainThread) { ObjectShadow.setMainThread(mainThread); }
    public static void initJOGLPanel(haven.JOGLPanel joglPanel) { ObjectShadow.setJOGLPanel(joglPanel); }
    public static void initDispatcher(haven.UIPanel.Dispatcher dispatcher) { ObjectShadow.setDispatcher(dispatcher); }
    public static void initUIRunner(haven.UI.Runner uiRunner) { ObjectShadow.setUIRunner(uiRunner); }
    public static void initUI(haven.UI ui) { ObjectShadow.setUI(ui); }
    public static void initRootWidget(haven.RootWidget rootWidget) { ObjectShadow.setRootWidget(rootWidget); }
    public static void initGameUI(haven.GameUI gameUI) { ObjectShadow.setGameUI(gameUI); }
    public static void initMapView(haven.MapView mapView) { ObjectShadow.setMapView(mapView); }
    public static void initSession(haven.Session session) { ObjectShadow.setSession(session); }
    public static void initCharList(haven.Charlist characterList) { ObjectShadow.setCharacterList(characterList); }
    public static void initGlob(haven.Glob glob) { ObjectShadow.setGlob(glob); }
    public static void initObjectCache(haven.OCache objectCache) { ObjectShadow.setObjectCache(objectCache); }

    public static void initGaugeArray(haven.IMeter gauge) { ObjectShadow.setGaugeArray(gauge); }
}
