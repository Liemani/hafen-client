package lmi;

public class ObjectShadow {
    // static fields
    public static Thread mainThread_;
    public static haven.MainFrame mainFrame_;
    public static haven.JOGLPanel joglPanel_;
    public static haven.UIPanel.Dispatcher dispatcher_;
    public static haven.UI.Runner uiRunner_;
    public static haven.UI ui_;
    // useful
    // ui_.mc: current mouse location as haven.Coord

    // init()
    static void init(Object ... args) {
        mainFrame_ = (haven.MainFrame)args[0];
        joglPanel_ = (haven.JOGLPanel)args[1];
        mainThread_ = (Thread)args[2];
    }
}
