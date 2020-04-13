package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] buckets;
    private int size;
    private int threshold;

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
        size = 0;
    }

    static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> prev;

        public Node(K key, V value, Node<K, V> prev) {
            this.key = key;
            this.value = value;
            this.prev = prev;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            newSize();
        }
        int bucketIndex = getBucketIndex(key);
        Node<K, V> node = getBucket(key, bucketIndex);
        if (node != null) {
            node.value = value;
        } else {
            Node<K, V> newBucket = new Node<>(key, value, buckets[bucketIndex]);
            buckets[bucketIndex] = newBucket;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getBucket(key, getBucketIndex(key));
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void newSize() {
        size = 0;
        Node<K, V>[] biggerBuckets = new Node[buckets.length * 2];
        threshold = (int) (biggerBuckets.length * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] tempBucket = buckets;
        buckets = biggerBuckets;
        for (Node<K, V> node : tempBucket) {
            while (node != null) {
                put(node.key, node.value);
                node = node.prev;
            }
        }
    }

    private int getHash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode());
    }

    private int getBucketIndex(K key) {
        return getHash(key) % buckets.length;
    }

    private Node getBucket(K key, int index) {
        Node<K, V> bucket = buckets[index];
        while (bucket != null) {
            if (Objects.equals(key, bucket.key)) {
                return bucket;
            }
            bucket = bucket.prev;
        }
        return null;
    }
}
