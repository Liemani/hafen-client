package lmi;

import haven.*;

import static lmi.Constant.gfx.hud.meter.*;
import static lmi.Constant.Gauge.Index.*;

public class ObjectShadow {
    // Field
    private static MainFrame _mainFrame;
    private static Thread _mainThread;
    private static JOGLPanel _joglPanel;
    private static UIPanel.Dispatcher _dispatcher;
    private static UI.Runner _uiRunner;
    private static UI _ui;
    private static RootWidget _rootWidget;
    private static GameUI _gameUI;
    private static MapView _mapView;
    private static Session _session;
    private static Charlist _characterList;
    private static IMeter[] _gaugeWidgetArray;
    private static Glob _glob;
    private static OCache _objectCache;

    // Initialize
    static void init() { _gaugeWidgetArray = new IMeter[3]; }

    // Getter
    public static MainFrame mainFrame() { return _mainFrame; }
    public static Thread mainThread() { return _mainThread; }
    public static JOGLPanel joglPanel() { return _joglPanel; }
    public static UIPanel.Dispatcher dispatcher() { return _dispatcher; }
    public static UI.Runner uiRunner() { return _uiRunner; }
    public static UI ui() { return _ui; }
    public static RootWidget rootWidget() { return _rootWidget; }
    public static GameUI gameUI() { return _gameUI; }
    public static MapView mapView() { return _mapView; }
    public static Session session() { return _session; }
    public static Charlist characterList() { return _characterList; }
    public static IMeter[] gaugeWidgetArray() { return _gaugeWidgetArray; }
    public static Glob glob() { return _glob; }
    public static OCache objectCache() { return _objectCache; }

    // Setter
    static void setMainFrame(MainFrame mainFrame) { _mainFrame = mainFrame; }
    static void setMainThread(Thread mainThread) { _mainThread = mainThread; }
    static void setJOGLPanel(JOGLPanel joglPanel) { _joglPanel = joglPanel; }
    static void setDispatcher(UIPanel.Dispatcher dispatcher) { _dispatcher = dispatcher; }
    static void setUIRunner(UI.Runner uiRunner) { _uiRunner = uiRunner; }
    static void setUI(UI ui) { _ui = ui; }
    static void setRootWidget(RootWidget rootWidget) { _rootWidget = rootWidget; }
    static void setGameUI(GameUI gameUI) { _gameUI = gameUI; }
    static void setMapView(MapView mapView) {
        _mapView = mapView;
        Util.initMapViewCenterByMapView(_mapView);
    }
    static void setSession(Session session) { _session = session; }
    static void setCharacterList(Charlist characterList) { _characterList = characterList; }
    static void setGlob(Glob glob) { _glob = glob; }
    static void setObjectCache(OCache objectCache) { _objectCache = objectCache; }

    static void setGaugeArray(IMeter gauge) {
        final String resourceName = gauge.resourceName();
        if (resourceName.endsWith(RN_HIT_POINT))
            _gaugeWidgetArray[GI_HIT_POINT] = gauge;
        else if (resourceName.endsWith(RN_STAMINA))
            _gaugeWidgetArray[GI_STAMINA] = gauge;
        else if (resourceName.endsWith(RN_ENERGY))
            _gaugeWidgetArray[GI_ENERGY] = gauge;
    }

    // Package Method
    static Coord getMouseLocation() {
        return _ui.mc;
    }

    static void interruptMainThread() {
        lmi.ObjectShadow._mainThread.interrupt();
    }

    static void closeSession() {
        _ui.sess.close();
    }
}
