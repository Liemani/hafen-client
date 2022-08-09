package lmi;

public class Initializer {
    public static void init() {
        Scanner.init(System.in);
        Command.init();
        ObjectFinder.init();
        new Thread(new lmi.MainRunnable()).start();
    }

    public static void initMainFrame(haven.MainFrame mainFrame) { ObjectShadow.mainFrame_ = mainFrame; }
    public static void initMainThread(Thread mainThread) { ObjectShadow.mainThread_ = mainThread; }
    public static void initJOGLPanel(haven.JOGLPanel joglPanel) { ObjectShadow.joglPanel_ = joglPanel; }
    public static void initDispatcher(haven.UIPanel.Dispatcher dispatcher) { ObjectShadow.dispatcher_ = dispatcher; }
    public static void initUIRunner(haven.UI.Runner uiRunner) { ObjectShadow.uiRunner_ = uiRunner; }
    public static void initUI(haven.UI ui) { ObjectShadow.ui_ = ui; }
    public static void initRootWidget(haven.RootWidget rootWidget) { ObjectShadow.rootWidget_ = rootWidget; }
    public static void initGameUI(haven.GameUI gameUI) { ObjectShadow.gameUI_ = gameUI; }
    public static void initMapView(haven.MapView mapView) { ObjectShadow.mapView_ = mapView; }
    public static void initSession(haven.Session session) { ObjectShadow.session_ = session; }
    public static void initCharList(haven.Charlist characterList) { ObjectShadow.characterList_ = characterList; }
}
