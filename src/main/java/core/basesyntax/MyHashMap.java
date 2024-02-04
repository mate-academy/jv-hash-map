package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int INCREASE_RATIO = 2;
    private int threshold;
    private final float loadFactor;
    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
        if (loadFactor < 0 || loadFactor > 1) {
            throw new IllegalArgumentException("Illegal Load: " + loadFactor);
        }
        buckets = (Node<K, V>[]) new Node[initialCapacity];
        this.loadFactor = loadFactor;
        threshold = (int) (initialCapacity * loadFactor);
    }

    @Override
    public void put(K key, V value) {
        int index = computeIndex(key);
        Node<K, V> nodeToBeAdded = new Node<>(key, value);
        if (size == threshold) {
            resize();
        }
        if (buckets[index] == null) {
            buckets[index] = nodeToBeAdded;
            size++;
            return;
        }
        Node<K, V> current = buckets[index];
        while (current != null) {
            if (Objects.equals(key, current.key)) {
                current.value = value;
                return;
            } else {
                current = current.next;
            }
        }
        nodeToBeAdded.next = buckets[index];
        buckets[index] = nodeToBeAdded;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = computeIndex(key);
        Node<K, V> current = buckets[index];
        while (current != null) {
            if (Objects.equals(key, current.key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private int computeIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % buckets.length);
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldBuckets = buckets;
        int newCapacity = buckets.length * INCREASE_RATIO;
        buckets = (Node<K, V>[]) new Node[newCapacity];
        for (Node<K, V> current : oldBuckets) {
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }
}
