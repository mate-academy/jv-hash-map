package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
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

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

    }

    @Override
    public void put(K key, V value) {
        int index = computeIndex(key);
        Node<K, V> current = buckets[index];
        while (current != null) {
            if (Objects.equals(key, current.key)) {
                current.value = value;
                return;
            } else {
                current = current.next;
            }
        }
        if (size > threshold) {
            resize();
            index = computeIndex(key);
        }
        addNode(key, value, index);
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

    private int computeIndex(K key) {
        return Math.abs(getHash(key) % buckets.length);
    }

    private void resize() {
        Node<K, V>[] oldBuckets = buckets;
        int newCapacity = buckets.length * 2;
        threshold = (int) (newCapacity * loadFactor);
        buckets = (Node<K, V>[]) new Node[newCapacity];
        for (Node<K, V> oldBucket : oldBuckets) {
            Node<K, V> current = oldBucket;
            while (current != null) {
                int index = computeIndex(current.key);
                addNode(current.key, current.value, index);
                current = current.next;
            }
        }
    }

    private void addNode(K key, V value, int index) {
        Node<K, V> nodeToBeAdded = new Node<>(key, value);
        nodeToBeAdded.next = buckets[index];
        buckets[index] = nodeToBeAdded;
    }

    private static int getHash(Object key) {
        return key == null ? 0 : key.hashCode();
    }
}
