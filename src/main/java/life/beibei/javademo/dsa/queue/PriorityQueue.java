package life.beibei.javademo.dsa.queue;

import java.util.*;

public class PriorityQueue<E> implements Queue<E> {

    private int size;

    private Object [] elementData;

    private Comparator<? super E> comparator;

    private static final int DEFAULT_CAPACITY = 10;

    /**
     * The maximum size of array to allocate.
     * Some VMs reserve some header words in an array.
     * Attempts to allocate larger arrays may result in
     * OutOfMemoryError: Requested array size exceeds VM limit
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    public PriorityQueue(int initCapacity){
        this.elementData = new Object[initCapacity];
    }

    public PriorityQueue(){
        this.elementData = new Object[DEFAULT_CAPACITY];
    }

    public  PriorityQueue(int initCapacity, Comparator<? super E> comparator){
        this.elementData = new Object[initCapacity];
        this.comparator = comparator;
    }

    public PriorityQueue(Comparator<? super E> comparator){
        this.comparator = comparator;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        return (T[]) this.elementData;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1);
        int index = this.size;
        // 如果用户提供了Comparator
        if(comparator != null){
            while (index > 0){
                int parentIndex = (index+1) / 2 - 1;
                E parent = (E) elementData[parentIndex];
                if(comparator.compare(e, parent) > 0){
                    elementData[index] = parent;
                    index = parentIndex;
                }else {
                    break;
                }
            }
            // 不提供Comparator
        }else {
            while (index > 0){
                Comparable<? super E> curr = (Comparable<? super E>) e;
                int parentIndex = (index+1) / 2 - 1;
                E parent = (E) elementData[parentIndex];
                if(curr.compareTo(parent) > 0){
                    elementData[index] = parent;
                    index =parentIndex;
                }else {
                    break;
                }
            }
        }
        elementData[index] = e;
        this.size++;
        return true;
    }

    /**
     * 直接使用ArrayList扩展函数
     * @param minCapacity 最少需要的空间大小
     */
    private void ensureCapacity(int minCapacity){
        // overflow-conscious code
        if (minCapacity - elementData.length > 0){
            // overflow-conscious code
            int oldCapacity = elementData.length;
            int newCapacity = oldCapacity + (oldCapacity >> 1);
            if (newCapacity - minCapacity < 0)
                newCapacity = minCapacity;
            if (newCapacity - MAX_ARRAY_SIZE > 0)
                newCapacity = hugeCapacity(minCapacity);
            // minCapacity is usually close to size, so this is a win:
            elementData = Arrays.copyOf(elementData, newCapacity);
        }
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
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
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        this.elementData = new Object[DEFAULT_CAPACITY];
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E remove() {
        E top = poll();
        if(top == null){
            throw new NoSuchElementException();
        }
        return top;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E poll() {
        if(this.size == 0 ) {
            return null;
        }
        E top = (E)elementData[0];
        elementData[0] = elementData[size - 1];
        int index = 0;
        while (index < size-1){
            int rightIndex = (index + 1) * 2;
            int leftIndex = rightIndex - 1;
            if(leftIndex >= size){
                break;
            }
            int maxIndex = leftIndex;
            if(rightIndex < size && comparator != null && comparator.compare((E)elementData[rightIndex], (E)elementData[leftIndex]) > 0){
                maxIndex = rightIndex;
            }
            if(rightIndex < size && comparator == null && ((Comparable<? super E>)elementData[rightIndex]).compareTo((E)elementData[leftIndex]) > 0){
                maxIndex = rightIndex;
            }
            if(comparator != null && comparator.compare((E)elementData[maxIndex], (E)elementData[index]) > 0){
                elementData[index] = elementData[maxIndex];
                elementData[maxIndex] = elementData[size -1];
                index = maxIndex;
            }else if(comparator != null){
                break;
            }
            if(comparator == null && ((Comparable<? super E>)elementData[maxIndex]).compareTo((E)elementData[index]) > 0){
                elementData[index] = elementData[maxIndex];
                elementData[maxIndex] = elementData[size -1];
                index = maxIndex;
            }else if(comparator == null){
                break;
            }
        }
        elementData[size - 1] = null;
        this.size--;
        return top;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E element() {
        if(this.size == 0){
            throw new NoSuchElementException();
        }
        return (E)elementData[0];
    }

    @SuppressWarnings("unchecked")
    @Override
    public E peek() {
        if(this.size == 0) {
            return null;
        }
        return (E)elementData[0];
    }

    private class Itr implements Iterator<E>{
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return cursor + 1 < size;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @SuppressWarnings("unchecked")
        @Override
        public E next() {
            return (E)elementData[cursor++];
        }
    }
}
