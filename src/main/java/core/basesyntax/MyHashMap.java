package core.basesyntax;

import java.util.ArrayList;
import java.util.List;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final float GROW_RATE = 2.0f;
    private int capacity = DEFAULT_CAPACITY;
    private int threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    private int size;
    private Node<K, V>[] buckets;

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        if (buckets[index] == null) {
            buckets[index] = new Node<>(key, value);
            resizeIfThresholdExceeded();
            return;
        }
        Node<K, V> bucket = buckets[index];
        Node<K, V> nextBucket = bucket;
        do {
            if (shouldReplaceValue(nextBucket, key)) {
                nextBucket.value = value;
                return;
            }
            bucket = nextBucket;
            nextBucket = nextBucket.next;
        } while (nextBucket != null);
        bucket.next = new Node<>(key, value);
        resizeIfThresholdExceeded();
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        if (buckets[index] == null) {
            return null;
        }
        Node<K, V> bucket = buckets[index];
        while (bucket != null) {
            if (shouldReplaceValue(bucket, key)) {
                return bucket.value;
            }
            bucket = bucket.next;
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

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return key.hashCode() >= 0 ? key.hashCode() % capacity : (key.hashCode() * -1) % capacity;
    }

    private void resizeIfThresholdExceeded() {
        size++;
        if (size == threshold) {
            resize();
        }
    }

    private void resize() {
        capacity = (int) (capacity * GROW_RATE);
        threshold = (int) (capacity * LOAD_FACTOR);
        List<Node<K, V>> nodes = entrySet();
        buckets = new Node[capacity];
        size = 0;
        for (Node<K, V> node : nodes) {
            put(node.key, node.value);
        }
    }

    private List<Node<K, V>> entrySet() {
        List<Node<K, V>> nodes = new ArrayList<>();
        for (Node<K, V> node : buckets) {
            while (node != null) {
                nodes.add(node);
                node = node.next;
            }
        }
        return nodes;
    }

    private boolean shouldReplaceValue(Node<K, V> bucket, K key) {
        return (bucket.key != null && bucket.key.equals(key))
                || bucket.key == key;
    }
}
