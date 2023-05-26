package core.basesyntax;

import java.util.ArrayList;
import java.util.List;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private List<Node<K, V>>[] buckets;
    private int size;
    private int capacity;

    public MyHashMap() {
        capacity = INITIAL_CAPACITY;
        buckets = (List<Node<K, V>>[]) new List[capacity];
        initializeBuckets(buckets);
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putForNullKey(value);
            return;
        }

        int bucketIndex = getBucketIndex(key, capacity);
        List<Node<K, V>> bucket = buckets[bucketIndex];
        Node<K, V> node = getNodeByKey(key, bucket);
        if (node != null) {
            node.value = value;
        } else {
            bucket.add(new Node<>(key, value));
            size++;

            if (size >= capacity * LOAD_FACTOR) {
                resize();
            }
        }
    }

    private void putForNullKey(V value) {
        List<Node<K, V>> nullKeyBucket = buckets[0];
        Node<K, V> node = getNodeByKey(null, nullKeyBucket);
        if (node != null) {
            node.value = value;
        } else {
            nullKeyBucket.add(new Node<>(null, value));
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getValueForNullKey();
        }

        int bucketIndex = getBucketIndex(key, capacity);
        List<Node<K, V>> bucket = buckets[bucketIndex];
        Node<K, V> node = getNodeByKey(key, bucket);
        return node != null ? node.value : null;
    }

    private V getValueForNullKey() {
        List<Node<K, V>> nullKeyBucket = buckets[0];
        Node<K, V> node = getNodeByKey(null, nullKeyBucket);
        return node != null ? node.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getBucketIndex(K key, int bucketCount) {
        return key == null ? 0 : Math.abs(key.hashCode()) % bucketCount;
    }

    private Node<K, V> getNodeByKey(K key, List<Node<K, V>> bucket) {
        for (Node<K, V> node : bucket) {
            if ((key == null && node.key == null) || (key != null && key.equals(node.key))) {
                return node;
            }
        }
        return null;
    }

    private void resize() {
        int newCapacity = capacity * 2;
        List<Node<K, V>>[] newBuckets = (List<Node<K, V>>[]) new List[newCapacity];
        initializeBuckets(newBuckets);

        for (List<Node<K, V>> bucket : buckets) {
            for (Node<K, V> node : bucket) {
                int bucketIndex = getBucketIndex(node.key, newCapacity);
                newBuckets[bucketIndex].add(node);
            }
        }

        buckets = newBuckets;
        capacity = newCapacity;
    }

    private void initializeBuckets(List<Node<K, V>>[] buckets) {
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new ArrayList<>();
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}


