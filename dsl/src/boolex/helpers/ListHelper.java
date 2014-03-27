package boolex.helpers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajr64 on 3/26/14.
 */
public class ListHelper {
    public static <E> List<E> exclude(List<E> list, E undesirable) {
        List<E> subList = new ArrayList<>();
        for (E element : list) {
            if (element != undesirable)
                subList.add(element);
        }
        return subList;
    }
}