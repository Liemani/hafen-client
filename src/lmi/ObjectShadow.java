package lmi;

class ObjectShadow {
    // MainFrame
    static haven.MainFrame mainFrame_;
    static haven.UIPanel uiPanel_;
    static Thread mainThread_;

    // init()
    static void init(Object ... args) {
        mainFrame_ = (haven.MainFrame)args[0];
        uiPanel_ = (haven.UIPanel)args[1];
        mainThread_ = (Thread)args[2];
    }
}
