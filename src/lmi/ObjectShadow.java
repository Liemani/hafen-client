package lmi;

public class ObjectShadow {
    // static fields
    static haven.MainFrame mainFrame_;   // haven.MainFrame::MainFrame()
    static Thread mainThread_;   // haven.MainFrame::run()
    static haven.JOGLPanel joglPanel_;   // haven.JOGLPanel::JOGLPanel()
    static haven.UIPanel.Dispatcher dispatcher_; // haven.JOGLPanel::JOGLPanel()
    static haven.UI.Runner uiRunner_;    // haven.Bootstrap::Bootstrap(), haven.RemoteUI::RemoteUI()
    static haven.UI ui_; // haven.UI::UI()
    static haven.RootWidget rootWidget_; // haven.RootWidget::RootWidget()
    static haven.GameUI gameUI_; // haven.GameUI::GameUI()
    static haven.MapView mapView_;   // haven.MapView::MapView()
    static haven.Session session_;   // haven.Session::Session()
    static haven.Charlist characterList_;    // haven.Charlist::Charlist()
    static haven.IMeter[] gaugeWidgetArray_;  // haven.IMeter::IMeter()
    static haven.Glob glob_;    // haven.Glob::Glob()

    // initialize
    static void init() { gaugeWidgetArray_ = new haven.IMeter[3]; }

    // getter
    public static haven.MainFrame mainFrame() { return mainFrame_; }
    public static Thread mainThread() { return mainThread_; }
    public static haven.JOGLPanel joglPanel() { return joglPanel_; }
    public static haven.UIPanel.Dispatcher dispatcher() { return dispatcher_; }
    public static haven.UI.Runner uiRunner() { return uiRunner_; }
    public static haven.UI ui() { return ui_; }
    public static haven.RootWidget rootWidget() { return rootWidget_; }
    public static haven.GameUI gameUI() { return gameUI_; }
    public static haven.MapView mapView() { return mapView_; }
    public static haven.Session session() { return session_; }
    public static haven.Charlist characterList() { return characterList_; }
    public static haven.IMeter[] gaugeWidgetArray() { return gaugeWidgetArray_; }
    public static haven.Glob glob() { return glob_; }

    // setter
    static void setMainFrame(haven.MainFrame mainFrame) { mainFrame_ = mainFrame; }
    static void setMainThread(Thread mainThread) { mainThread_ = mainThread; }
    static void setJOGLPanel(haven.JOGLPanel joglPanel) { joglPanel_ = joglPanel; }
    static void setDispatcher(haven.UIPanel.Dispatcher dispatcher) { dispatcher_ = dispatcher; }
    static void setUIRunner(haven.UI.Runner uiRunner) { uiRunner_ = uiRunner; }
    static void setUI(haven.UI ui) { ui_ = ui; }
    static void setRootWidget(haven.RootWidget rootWidget) { rootWidget_ = rootWidget; }
    static void setGameUI(haven.GameUI gameUI) { gameUI_ = gameUI; }
    static void setMapView(haven.MapView mapView) {
        mapView_ = mapView;
        lmi.api.Util.initMapViewCenterByMapView(mapView_);
    }
    static void setSession(haven.Session session) { session_ = session; }
    static void setCharacterList(haven.Charlist characterList) { characterList_ = characterList; }
    static void setGlob(haven.Glob glob) { glob_ = glob; }

    static void setGaugeArray(haven.IMeter gauge) {
        if (Util.resourceName(gauge).equals(Constant.Gauge.ResourceName.HIT_POINT))
            gaugeWidgetArray_[Constant.Gauge.Index.HIT_POINT] = gauge;
        else if (Util.resourceName(gauge).equals(Constant.Gauge.ResourceName.STAMINA))
            gaugeWidgetArray_[Constant.Gauge.Index.STAMINA] = gauge;
        else if (Util.resourceName(gauge).equals(Constant.Gauge.ResourceName.ENERGY))
            gaugeWidgetArray_[Constant.Gauge.Index.ENERGY] = gauge;
    }

    // package methods
    static haven.Coord getMouseLocation() {
        return ui_.mc;
    }

    static void interruptMainThread() {
        lmi.ObjectShadow.mainThread_.interrupt();
    }

    static void closeSession() {
        ui_.sess.close();
    }
}
