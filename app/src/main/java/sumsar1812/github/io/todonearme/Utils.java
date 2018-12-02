package sumsar1812.github.io.todonearme;

import java.text.DecimalFormat;

public class Utils {
    private static Utils instance;
    private Utils() {}
    public static Utils getInstance() {
        if (instance == null)
            instance = new Utils();
        return instance;
    }
    public String getDistanceString(float value) {
        DecimalFormat df = new DecimalFormat("#.00");
        if (value > 1000) {
            value = value / 1000;
            return df.format(value) + " km";
        } else {
            return df.format(value) + " m";
        }
    }
}
