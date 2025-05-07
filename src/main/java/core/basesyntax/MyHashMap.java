package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int CAPACITY_MULTIPLIER = 2;

    private Node<K, V>[] buckets;

    private int size;
    private int capacity = DEFAULT_CAPACITY;
    private double threshold = capacity * LOAD_FACTOR;

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value);
        Node<K, V> currentBucket = buckets[getIndex(key)];
        int index = getIndex(key);

        if (currentBucket != null) {
            while (currentBucket != null) {
                if ((currentBucket.key != null && currentBucket.key.equals(key))
                        || (currentBucket.key == null && key == null)) {
                    currentBucket.value = value;
                    return;
                }
                currentBucket = currentBucket.next;
            }
            newNode.next = buckets[index];
            buckets[index] = newNode;

        } else {
            buckets[index] = newNode;
        }
        size++;
        if (size >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> current = buckets[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
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

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        int hash = key.hashCode();
        return Math.abs(hash) % capacity;
    }

    private void resize() {
        final int newCapacity = capacity * CAPACITY_MULTIPLIER;
        Node<K, V>[] newBuckets = new Node[newCapacity];
        threshold = newCapacity * LOAD_FACTOR;
        capacity = newCapacity;

        for (Node<K, V> node : buckets) {
            while (node != null) {
                Node<K, V> next = node.next;
                int index = getIndex(node.key);
                node.next = newBuckets[index];
                newBuckets[index] = node;
                node = next;
            }
        }

        buckets = newBuckets;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

}
