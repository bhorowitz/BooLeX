package boolex.helpers;

/**
 * Created by dani on 3/14/14.
 */
public class PrettyPrintHelper {
    /**
     * Creates a nicely formatted array for printing
     * @param arr The array to stringify
     * @return A nicely formatted string with array contents
     */
    public static String arrayToString(Object[] arr) {
        if (arr == null)
            return "null";
        String arrayAsString = "{ ";
        for (int i = 0; i < arr.length-1; i++) {
            arrayAsString += arr[i].toString() + ", ";
        }
        if (arr.length > 0)
            arrayAsString += arr[arr.length-1].toString() + " ";
        arrayAsString += "}";
        return arrayAsString;
    }
}
