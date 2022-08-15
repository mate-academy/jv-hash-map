package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_COEFFICIENT = 2;
    private Node<K, V>[] buckets;
    private int size;
    private int threshold;
    private int capacity;

    public MyHashMap() {
        buckets = new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
        capacity = INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value);
        int indexOfBucket = getIndexOfBucket(key);
        Node<K, V> currentNode = buckets[indexOfBucket];
        if (currentNode == null) {
            buckets[indexOfBucket] = newNode;
            size++;
            return;
        }
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next != null) {
                currentNode = currentNode.next;
            } else {
                currentNode.next = newNode;
                size++;
                return;
            }
        }

    }

    @Override
    public V getValue(K key) {
        int indexOfBucket = getIndexOfBucket(key);
        Node<K, V> currentNode = buckets[indexOfBucket];
        if (currentNode == null) {
            return null;
        }
        if (Objects.equals(currentNode.key, key)) {
            return currentNode.value;
        }
        while (currentNode.next != null) {
            currentNode = currentNode.next;
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndexOfBucket(K key) {
        return (key == null) ? 0
                : Math.abs(key.hashCode()) % 16;
    }

    private void resize() {
        capacity *= RESIZE_COEFFICIENT;
        size = 0;
        Node<K, V>[] previousBuckets = buckets;
        buckets = new Node[capacity];
        threshold = (int) (capacity * LOAD_FACTOR);
        for (Node<K, V> bucket : previousBuckets) {
            if (bucket == null) {
                continue;
            }
            Node<K, V> currentNode = bucket;
            put(currentNode.key, currentNode.value);
            while (currentNode.next != null) {
                currentNode = currentNode.next;
                put(currentNode.key, currentNode.value);
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
