package lmi;

public class ObjectShadow {
    // static fields
    public static haven.MainFrame mainFrame_;   // haven.MainFrame::MainFrame()
    public static Thread mainThread_;   // haven.MainFrame::run()
    public static haven.JOGLPanel joglPanel_;   // haven.JOGLPanel::JOGLPanel()
    public static haven.UIPanel.Dispatcher dispatcher_; // haven.JOGLPanel::JOGLPanel()
    public static haven.UI.Runner uiRunner_;    // haven.Bootstrap::Bootstrap(), haven.RemoteUI::RemoteUI()
    public static haven.UI ui_; // haven.UI::UI()
    public static haven.RootWidget rootWidget_; // haven.RootWidget::RootWidget()
    public static haven.GameUI gameUI_; // haven.GameUI::GameUI()
    public static haven.MapView mapView_;   // haven.MapView::MapView()
    public static haven.Session session_;   // haven.Session::Session()
    public static haven.Charlist characterList_;    // haven.Charlist::Charlist()
    public static haven.IMeter[] gaugeArray_;  // haven.IMeter::IMeter()

    static void init() {
        gaugeArray_ = new haven.IMeter[3];
    }

    // initializer
    static void initMainFrame(haven.MainFrame mainFrame) { mainFrame_ = mainFrame; }
    static void initMainThread(Thread mainThread) { mainThread_ = mainThread; }
    static void initJOGLPanel(haven.JOGLPanel joglPanel) { joglPanel_ = joglPanel; }
    static void initDispatcher(haven.UIPanel.Dispatcher dispatcher) { dispatcher_ = dispatcher; }
    static void initUIRunner(haven.UI.Runner uiRunner) { uiRunner_ = uiRunner; }
    static void initUI(haven.UI ui) { ui_ = ui; }
    static void initRootWidget(haven.RootWidget rootWidget) { rootWidget_ = rootWidget; }
    static void initGameUI(haven.GameUI gameUI) { gameUI_ = gameUI; }
    static void initMapView(haven.MapView mapView) {
        mapView_ = mapView;
        lmi.api.Util.initMapViewCenterByMapView(mapView_);
    }
    static void initSession(haven.Session session) { session_ = session; }
    static void initCharacterList(haven.Charlist characterList) { characterList_ = characterList; }

    static void setGaugeArray(haven.IMeter gauge) {
        if (Util.resourceName(gauge).equals(Constant.Gauge.ResourceName.HIT_POINT))
            gaugeArray_[Constant.Gauge.Index.HIT_POINT] = gauge;
        else if (Util.resourceName(gauge).equals(Constant.Gauge.ResourceName.STAMINA))
            gaugeArray_[Constant.Gauge.Index.STAMINA] = gauge;
        else if (Util.resourceName(gauge).equals(Constant.Gauge.ResourceName.ENERGY))
            gaugeArray_[Constant.Gauge.Index.ENERGY] = gauge;
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
