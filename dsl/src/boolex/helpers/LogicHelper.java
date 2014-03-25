package boolex.helpers;

/**
 * Created by dani on 2/17/14.
 */
public class LogicHelper {
    public static boolean isNull(Boolean value) {
        return value == null;
    }

    public static boolean isFalse(Boolean value) {
        return value != null && !value;
    }

    public static boolean isTrue(Boolean value) {
        return value != null && value;
    }
}
