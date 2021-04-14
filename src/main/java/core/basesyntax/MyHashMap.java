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

    public MyHashMap(int initialCapacity, float loadFactor) {
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

        entryTable = newBucketTable(capacity);
        this.loadFactor = loadFactor;
        threshold = (int) (capacity * loadFactor);
    }

    @Override
    public void put(K key, V value) {
        int numberOfBucket = getBucket(key);
        Node<K, V> element;
        if (size + 1 >= threshold) {
            reSize(entryTable.length * VALUE_FOR_INCREASE);
        }
        for (element = entryTable[numberOfBucket]; element != null; element = element.next) {
            if (numberOfBucket == element.hash && Objects.equals(key, element.nodeKey)) {
                V oldValue = element.nodeValue;
                if (value != oldValue) {
                    element.nodeValue = value;
                    return;
                }
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
        Node<K, V>[] table = getTable();
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

    private void reSize(int newCapacity) {
        if (size >= threshold / VALUE_FOR_INCREASE) {
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

    private Node<K, V>[] getTable() {
        return entryTable;
    }

    final int getBucket(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % entryTable.length;
    }

    private Node<K, V>[] newBucketTable(int capacity) {
        return (Node<K, V>[]) new Node<?, ?>[capacity];
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

        public K getKey() {
            return nodeKey;
        }

        public V getValue() {
            return nodeValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass().getName() != o.getClass().getName()) {
                return false;
            }
            Node<K, V> e = (Node<K, V>) o;
            K k1 = getKey();
            Object k2 = e.getKey();
            if (Objects.equals(k1, k2)) {
                V v1 = getValue();
                Object v2 = e.getValue();
                if (Objects.equals(v1, v2)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public int hashCode() {
            K k = getKey();
            V v = getValue();

            int result = 1;
            final int prime = 31;
            result = prime * result + ((k == null) ? 0 : k.hashCode());
            result = prime * result + ((v == null) ? 0 : v.hashCode());

            return result;
        }

        @Override
        public String toString() {
            return getKey() + " - " + getValue();
        }
    }
}
