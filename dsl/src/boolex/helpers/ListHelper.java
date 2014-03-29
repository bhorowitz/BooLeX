package boolex.helpers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajr64 on 3/26/14.
 */
public class ListHelper {
    public static <E> List<E> exclude(List<E> list, E undesirable) {
        return list.stream().filter(element -> element != undesirable).map(element -> element).collect(java.util.stream.Collectors.toList());
    }
}
