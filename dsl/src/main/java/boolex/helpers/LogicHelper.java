package boolex.helpers;

public class LogicHelper {
    /**
     * Check if a Boolean value is null
     * @param value The value to check
     * @return True if it is null, False otherwise
     */
    public static boolean isNull(Boolean value) {
        return value == null;
    }

    /**
     * Check if a Boolean value is false
     * @param value The value to check
     * @return True if it is false, False otherwise
     */
    public static boolean isFalse(Boolean value) {
        return value != null && !value;
    }

    /**
     * Check if a Boolean value is true
     * @param value The value to check
     * @return True if it is true, False otherwise
     */
    public static boolean isTrue(Boolean value) {
        return value != null && value;
    }
}
