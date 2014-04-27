package boolex.helpers;

import java.util.List;

public class ListHelper {
    /**
     * Filters out any undesirable element from a list
     * @param list The list to filter
     * @param undesirable The undesirable element
     * @return A copy of the list without the undesirable element
     */
    public static <E> List<E> exclude(List<E> list, E undesirable) {
        return list.stream().filter(element -> element != undesirable).collect(java.util.stream.Collectors.toList());
    }
}
