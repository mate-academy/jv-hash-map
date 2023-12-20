package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int RESIZE_VALUE = 2;
    private Node<K, V>[] buckets;
    private int size;
    private int threshold;

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
        int bucketIndex = calculateBucketIndex(key);
        Node<K, V> existing = buckets[bucketIndex];

        while (existing != null) {
            if (isKeysEqual(existing.key, key)) {
                existing.value = value;
                return;
            }
            existing = existing.next;
        }

        buckets[bucketIndex] = new Node<>(key, value, buckets[bucketIndex]);
        size++;
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = calculateBucketIndex(key);
        Node<K, V> node = buckets[bucketIndex];

        while (node != null) {
            if (isKeysEqual(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int calculateBucketIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % buckets.length;
    }

    private boolean isKeysEqual(K key1, K key2) {
        return Objects.equals(key1, key2);
    }

    private void resizeIfNeeded() {
        if (size >= threshold) {
            Node<K, V>[] oldBuckets = buckets;
            buckets = new Node[oldBuckets.length * RESIZE_VALUE];
            size = 0;
            threshold = (int) (buckets.length * DEFAULT_LOAD_FACTOR);

            for (Node<K, V> head : oldBuckets) {
                while (head != null) {
                    put(head.key, head.value);
                    head = head.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
