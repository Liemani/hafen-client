package haven;

// turnel class to access protected properties
public class LMI {
    public static java.util.List<LayerMeter.Meter> gaugeWidgetGaugeArray(IMeter gaugeWidget) {
        return gaugeWidget.meters;
    }

    public static byte[] resourceDrawableBuffer(ResDrawable resourceDrawable) {
        if (resourceDrawable == null)
            return null;
        final MessageBuf messageBuffer = resourceDrawable.sdt;
        final byte[] buffer = messageBuffer.rbuf;
        return buffer;
    }
}
