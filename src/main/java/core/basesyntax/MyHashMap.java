package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int MAXIMUM_CAPACITY = Integer.MAX_VALUE / 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int VALUE_FOR_INCREASE = 2;
    private Node<K, V>[] entryTable;
    private int size;
    private int threshold;
    private final float loadFactor;

    public MyHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    private MyHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Wrong initial capacity: " + initialCapacity);
        }

        if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }

        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Wrong load factor: " + loadFactor);
        }

        int capacity = 1;
        while (capacity < initialCapacity) {
            capacity++;
        }

        entryTable = new Node[capacity];
        this.loadFactor = loadFactor;
        threshold = (int) (capacity * loadFactor);
    }

    @Override
    public void put(K key, V value) {
        int numberOfBucket = getBucket(key);
        Node<K, V> element;
        if (size >= threshold) {
            changeSize(entryTable.length * VALUE_FOR_INCREASE);
        }
        for (element = entryTable[numberOfBucket]; element != null; element = element.next) {
            if (numberOfBucket == element.hash && Objects.equals(key, element.nodeKey)) {
                element.nodeValue = value;
                return;
            } else if (numberOfBucket == element.hash && element.next == null) {
                element.next = new Node<>(numberOfBucket, key, value, null);
                size++;
                return;
            }
        }

        entryTable[numberOfBucket] = new Node<>(numberOfBucket, key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        int numberOfBucket = getBucket(key);
        Node<K, V>[] table = entryTable;
        Node<K, V> element = table[numberOfBucket];
        while (element != null) {
            if (element.hash == numberOfBucket && Objects.equals(key, element.nodeKey)) {
                return element.nodeValue;
            }
            element = element.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void changeSize(int newCapacity) {
        if (size >= threshold) {
            threshold = (int) (newCapacity * loadFactor);
            makeTransfer();
        }
    }

    private void makeTransfer() {
        Node<K, V>[] oldTable = entryTable;
        entryTable = new Node[entryTable.length * VALUE_FOR_INCREASE];
        size = 0;
        for (Node<K, V> element : oldTable) {
            while (element != null) {
                put(element.nodeKey, element.nodeValue);
                element = element.next;
            }
        }
    }

    private int getBucket(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % entryTable.length;
    }

    static class Node<K, V> {
        private int hash;
        private K nodeKey;
        private V nodeValue;
        private Node<K, V> next;

        public Node(int hash, K k, V v, Node<K, V> next) {
            this.nodeKey = k;
            this.nodeValue = v;
            this.next = next;
            this.hash = hash;
        }
    }
}
