package boolex.helpers;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ajr64 on 3/26/14.
 */
public class ListHelper {
    public static <E> List<E> exclude(List<E> list, E undesirable) {
        return list.stream().filter(element -> element != undesirable)
                .map(element -> element).collect(Collectors.toList());
    }
}
