package haven;

import java.util.List;

// turnel class to access protected properties
public class LMI {
    public static List<LayerMeter.Meter> gaugeMeters(IMeter gauge) {
        return gauge.meters;
    }
}
