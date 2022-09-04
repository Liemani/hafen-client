package lmi;

import static lmi.Constant.gfx.hud.meter.*;
import static lmi.Constant.Gauge.Index.*;

public class ObjectShadow {
    // Field
    private static haven.MainFrame mainFrame_;   // haven.MainFrame::MainFrame()
    private static Thread mainThread_;   // haven.MainFrame::run()
    private static haven.JOGLPanel joglPanel_;   // haven.JOGLPanel::JOGLPanel()
    private static haven.UIPanel.Dispatcher dispatcher_; // haven.JOGLPanel::JOGLPanel()
    private static haven.UI.Runner uiRunner_;    // haven.Bootstrap::Bootstrap(), haven.RemoteUI::RemoteUI()
    private static haven.UI ui_; // haven.UI::UI()
    private static haven.RootWidget rootWidget_; // haven.RootWidget::RootWidget()
    private static haven.GameUI gameUI_; // haven.GameUI::GameUI()
    private static haven.MapView mapView_;   // haven.MapView::MapView()
    private static haven.Session session_;   // haven.Session::Session()
    private static haven.Charlist characterList_;    // haven.Charlist::Charlist()
    private static haven.IMeter[] gaugeWidgetArray_;  // haven.IMeter::IMeter()
    private static haven.Glob glob_;    // haven.Glob::Glob()
    private static haven.OCache objectCache_;

    // Initialize
    static void init() { gaugeWidgetArray_ = new haven.IMeter[3]; }

    // Getter
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
    public static haven.OCache objectCache() { return objectCache_; }

    // Setter
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
    static void setObjectCache(haven.OCache objectCache) { objectCache_ = objectCache; }

    static void setGaugeArray(haven.IMeter gauge) {
        final String resourceName = gauge.resourceName();
        if (resourceName.endsWith(RN_HIT_POINT))
            gaugeWidgetArray_[GI_HIT_POINT] = gauge;
        else if (resourceName.endsWith(RN_STAMINA))
            gaugeWidgetArray_[GI_STAMINA] = gauge;
        else if (resourceName.endsWith(RN_ENERGY))
            gaugeWidgetArray_[GI_ENERGY] = gauge;
    }

    // Package Method
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
