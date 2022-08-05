package lmi;

public class ObjectShadow {
    // static fields
    public static haven.MainFrame mainFrame_;
    public static Thread mainThread_;
    public static haven.JOGLPanel joglPanel_;
    public static haven.UIPanel.Dispatcher dispatcher_;
    public static haven.UI.Runner uiRunner_;
    public static haven.UI ui_;
    public static haven.RootWidget rootWidget_;
    public static haven.GameUI gameUI_;

    // init()
    static void init(Object ... args) {
        mainFrame_ = (haven.MainFrame)args[0];
        joglPanel_ = (haven.JOGLPanel)args[1];
        mainThread_ = (Thread)args[2];
    }

    // public methods
    public static void setUI(haven.UI ui) {
        ui_ = ui;
        setRootWidget(ui.root);
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

    // private methods
    private static void setRootWidget(haven.RootWidget rootWidget) {
        rootWidget_ = rootWidget;
    }
}
