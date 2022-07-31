package lmi;

public class ObjectShadow {
    // MainFrame
    public static haven.MainFrame mainFrame_;
    public static haven.UIPanel uiPanel_;
    public static Thread mainThread_;

    // init()
    static void init(Object ... args) {
        mainFrame_ = (haven.MainFrame)args[0];
        uiPanel_ = (haven.UIPanel)args[1];
        mainThread_ = (Thread)args[2];
    }
}
