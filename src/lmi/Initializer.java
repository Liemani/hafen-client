package lmi;

import haven.*;

public class Initializer {
    public static void init() {
        Console.setscmd("lmi", new Command());
        Console.setscmd("man", new Manual());
        Console.setscmd("dev", new Development());
        Scanner.init(System.in);
        ObjectFinder.init();
        ObjectShadow.init();
        Debug.init();
        WaitManager.init();
        AutomationManager.init();
        Development.init();
    }

    // set ObjectShadow
    // MainFrame::MainFrame()
    // MainFrame::run()
    // JOGLPanel::JOGLPanel()
    // JOGLPanel::JOGLPanel()
    // Bootstrap::Bootstrap(), RemoteUI::RemoteUI()
    // UI::UI()
    // RootWidget::RootWidget()
    // GameUI::GameUI()
    // MapView::MapView()
    // Session::Session()
    // Charlist::Charlist()
    // IMeter::IMeter()
    // Glob::Glob()
    public static void initMainFrame(MainFrame mainFrame) { ObjectShadow.setMainFrame(mainFrame); }
    public static void initMainThread(Thread mainThread) { ObjectShadow.setMainThread(mainThread); }
    public static void initJOGLPanel(JOGLPanel joglPanel) { ObjectShadow.setJOGLPanel(joglPanel); }
    public static void initDispatcher(UIPanel.Dispatcher dispatcher) { ObjectShadow.setDispatcher(dispatcher); }
    public static void initRemoteUI(RemoteUI remoteUI) { ObjectShadow.setRemoteUI(remoteUI); }
    public static void initUI(UI ui) { ObjectShadow.setUI(ui); }
    public static void initRootWidget(RootWidget rootWidget) { ObjectShadow.setRootWidget(rootWidget); }
    public static void initGameUI(GameUI gameUI) { ObjectShadow.setGameUI(gameUI); }
    public static void initMapView(MapView mapView) { ObjectShadow.setMapView(mapView); }
    public static void initSession(Session session) { ObjectShadow.setSession(session); }
    public static void initCharList(Charlist characterList) { ObjectShadow.setCharacterList(characterList); }
    public static void initGlob(Glob glob) { ObjectShadow.setGlob(glob); }
    public static void initObjectCache(OCache objectCache) { ObjectShadow.setObjectCache(objectCache); }

    public static void initGaugeArray(IMeter gauge) { ObjectShadow.setGaugeArray(gauge); }
}
