package haven;

// turnel class to access protected properties
public class LMI {
    public static java.util.List<LayerMeter.Meter> gaugeWidgetGaugeArray(IMeter gaugeWidget) {
        return gaugeWidget.meters;
    }
}
