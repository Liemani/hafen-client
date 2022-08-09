package lmi;

public class ObjectShadow {
    // static fields
    public static haven.MainFrame mainFrame_;
    public static Thread mainThread_;
    public static haven.JOGLPanel joglPanel_;
    public static haven.UIPanel.Dispatcher dispatcher_;
    public static haven.UI.Runner uiRunner_;
    public static haven.UI ui_; // haven.UI::UI()
    public static haven.RootWidget rootWidget_;
    public static haven.GameUI gameUI_; // haven.GameUI::GameUI()
    public static haven.MapView mapView_;   // haven.MapView::MapView()
    public static haven.Session session_;
    public static haven.Charlist characterList_;

    // init()
    static void init(Object ... args) {
        mainFrame_ = (haven.MainFrame)args[0];
        joglPanel_ = (haven.JOGLPanel)args[1];
        mainThread_ = (Thread)args[2];
    }

    // public methods
    public static void initUI(haven.UI ui) {
        ui_ = ui;
        initRootWidget(ui.root);
        initSession(ui_.sess);
    }

    public static void initRootWidget(haven.RootWidget rootWidget) { rootWidget_ = rootWidget; }
    public static void initGameUI(haven.GameUI gameUI) { gameUI_ = gameUI; }
    public static void initMapView(haven.MapView mapView) { mapView_ = mapView; }
    public static void initSession(haven.Session session) { session_ = session; }
    public static void initCharList(haven.Charlist characterList) { characterList_ = characterList; }

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
