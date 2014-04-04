package boolex.helpers;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by ajr64 on 3/31/14.
 */
public class StablePriorityQueue<T extends Comparable<T>> implements Queue<T> {
    private PriorityQueue<Entry<T>> priorityQueue;
    private Integer highestOrder;

    public StablePriorityQueue() {
        priorityQueue = new PriorityQueue<>();
        highestOrder = 0;
    }

    public int size() {
        return priorityQueue.size();
    }

    public boolean isEmpty() {
        return priorityQueue.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return priorityQueue.contains(o);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new DataIterator(priorityQueue.iterator());
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @NotNull
    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return false;
    }

    public void clear() {
        priorityQueue.clear();
    }

    public boolean add(T t) {
        boolean addSuccess = priorityQueue.add(new Entry<T>(t, highestOrder));
        highestOrder++;
        return addSuccess;
    }

    @Override
    public boolean offer(T t) {
        return false;
    }

    public T remove() {
        return priorityQueue.remove().getData();
    }

    public T poll() {
        Entry<T> entry = priorityQueue.poll();
        if (entry.getOrder().equals(highestOrder))
            highestOrder--;
        return entry.getData();
    }

    public T element() {
        return priorityQueue.element().getData();
    }

    public T peek() {
        Entry<T> entry = priorityQueue.peek();
        if (entry != null)
            return entry.getData();
        return null;
    }

    private class DataIterator implements Iterator<T> {
        private Iterator<Entry<T>> entryIterator;

        private DataIterator(Iterator<Entry<T>> entryIterator) {
            this.entryIterator = entryIterator;
        }

        @Override
        public boolean hasNext() {
            return entryIterator.hasNext();
        }

        @Override
        public T next() {
            return entryIterator.next().getData();
        }
    }

    private class Entry<S extends Comparable<S>> implements Comparable<Entry<S>> {
        private S data;
        private Integer order;

        private Entry(S data, Integer order) {
            this.data = data;
            this.order = order;
        }

        @Override
        public int compareTo(@NotNull Entry<S> o) {
            int dataComparison = this.getData().compareTo(o.getData());
            if (dataComparison != 0)
                return dataComparison;
            return this.getOrder().compareTo(o.getOrder());
        }

        public S getData() {
            return data;
        }

        public Integer getOrder() {
            return order;
        }
    }
}
