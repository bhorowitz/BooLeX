package boolex.helpers;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * StablePriorityQueue acts as a wrapper around a built-in PriorityBlockingQueue. It ensures that
 * elements with equal keys are returned in insertion order (ie. if A is inserted before B, and
 * key(A)=key(B) then A will dequeue first)
 *
 * @param <T> the type of element to hold
 * @author Alex Reinking
 */
public class StablePriorityQueue<T extends Comparable<T>> implements Queue<T> {
    private PriorityBlockingQueue<Entry<T>> priorityQueue;
    /**
     * highestOrder maintains the current highest ordinal in the queue. When the highest number
     * is dequeued, this number gets decreased. When an element is enqueued, its order is set to
     * highestOrder+1 and highestOrder is incremented.
     */
    private Integer highestOrder;

    public StablePriorityQueue() {
        priorityQueue = new PriorityBlockingQueue<>(1000);
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

    /**
     * DataIterator returns the data fields of the Entry's it iterates over.
     * It is an iterator-transformer.
     */
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

    /**
     * Entry is a small wrapper class used to maintain insertion ordering. It keeps two fields:
     * 1) A data field -- this is the actual object
     * 2) An ordering field -- this is used to disambiguate key clashes
     * @param <S> The data type the entry holds.
     */
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
