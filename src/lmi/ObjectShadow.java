package lmi;

import static lmi.Constant.gfx.hud.meter.*;
import static lmi.Constant.Gauge.Index.*;

public class ObjectShadow {
    // Field
    private static haven.MainFrame _mainFrame;   // haven.MainFrame::MainFrame()
    private static Thread _mainThread;   // haven.MainFrame::run()
    private static haven.JOGLPanel _joglPanel;   // haven.JOGLPanel::JOGLPanel()
    private static haven.UIPanel.Dispatcher _dispatcher; // haven.JOGLPanel::JOGLPanel()
    private static haven.UI.Runner _uiRunner;    // haven.Bootstrap::Bootstrap(), haven.RemoteUI::RemoteUI()
    private static haven.UI _ui; // haven.UI::UI()
    private static haven.RootWidget _rootWidget; // haven.RootWidget::RootWidget()
    private static haven.GameUI _gameUI; // haven.GameUI::GameUI()
    private static haven.MapView _mapView;   // haven.MapView::MapView()
    private static haven.Session _session;   // haven.Session::Session()
    private static haven.Charlist _characterList;    // haven.Charlist::Charlist()
    private static haven.IMeter[] _gaugeWidgetArray;  // haven.IMeter::IMeter()
    private static haven.Glob _glob;    // haven.Glob::Glob()
    private static haven.OCache _objectCache;

    // Initialize
    static void init() { _gaugeWidgetArray = new haven.IMeter[3]; }

    // Getter
    public static haven.MainFrame mainFrame() { return _mainFrame; }
    public static Thread mainThread() { return _mainThread; }
    public static haven.JOGLPanel joglPanel() { return _joglPanel; }
    public static haven.UIPanel.Dispatcher dispatcher() { return _dispatcher; }
    public static haven.UI.Runner uiRunner() { return _uiRunner; }
    public static haven.UI ui() { return _ui; }
    public static haven.RootWidget rootWidget() { return _rootWidget; }
    public static haven.GameUI gameUI() { return _gameUI; }
    public static haven.MapView mapView() { return _mapView; }
    public static haven.Session session() { return _session; }
    public static haven.Charlist characterList() { return _characterList; }
    public static haven.IMeter[] gaugeWidgetArray() { return _gaugeWidgetArray; }
    public static haven.Glob glob() { return _glob; }
    public static haven.OCache objectCache() { return _objectCache; }

    // Setter
    static void setMainFrame(haven.MainFrame mainFrame) { _mainFrame = mainFrame; }
    static void setMainThread(Thread mainThread) { _mainThread = mainThread; }
    static void setJOGLPanel(haven.JOGLPanel joglPanel) { _joglPanel = joglPanel; }
    static void setDispatcher(haven.UIPanel.Dispatcher dispatcher) { _dispatcher = dispatcher; }
    static void setUIRunner(haven.UI.Runner uiRunner) { _uiRunner = uiRunner; }
    static void setUI(haven.UI ui) { _ui = ui; }
    static void setRootWidget(haven.RootWidget rootWidget) { _rootWidget = rootWidget; }
    static void setGameUI(haven.GameUI gameUI) { _gameUI = gameUI; }
    static void setMapView(haven.MapView mapView) {
        _mapView = mapView;
        Util.initMapViewCenterByMapView(_mapView);
    }
    static void setSession(haven.Session session) { _session = session; }
    static void setCharacterList(haven.Charlist characterList) { _characterList = characterList; }
    static void setGlob(haven.Glob glob) { _glob = glob; }
    static void setObjectCache(haven.OCache objectCache) { _objectCache = objectCache; }

    static void setGaugeArray(haven.IMeter gauge) {
        final String resourceName = gauge.resourceName();
        if (resourceName.endsWith(RN_HIT_POINT))
            _gaugeWidgetArray[GI_HIT_POINT] = gauge;
        else if (resourceName.endsWith(RN_STAMINA))
            _gaugeWidgetArray[GI_STAMINA] = gauge;
        else if (resourceName.endsWith(RN_ENERGY))
            _gaugeWidgetArray[GI_ENERGY] = gauge;
    }

    // Package Method
    static haven.Coord getMouseLocation() {
        return _ui.mc;
    }

    static void interruptMainThread() {
        lmi.ObjectShadow._mainThread.interrupt();
    }

    static void closeSession() {
        _ui.sess.close();
    }
}
