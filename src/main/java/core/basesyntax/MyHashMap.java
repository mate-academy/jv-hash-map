package core.basesyntax;

import java.util.LinkedList;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private LinkedList<Node<K, V>>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new LinkedList[DEFAULT_CAPACITY];
        size = 0;
    }

    private static class Node<K, V> {
        private K key;
        private V value;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void put(K key, V value) {
        int bucketIndex = calculateBucketIndex(key);
        if (buckets[bucketIndex] == null) {
            buckets[bucketIndex] = new LinkedList<>();
        }

        for (Node<K, V> node : buckets[bucketIndex]) {
            if (isKeysEqual(node.key, key)) {
                node.value = value;
                return;
            }
        }

        buckets[bucketIndex].add(new Node<>(key, value));
        size++;

        if (shouldResize()) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = calculateBucketIndex(key);
        LinkedList<Node<K, V>> bucket = buckets[bucketIndex];

        if (bucket != null) {
            for (Node<K, V> node : bucket) {
                if (isKeysEqual(node.key, key)) {
                    return node.value;
                }
            }
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
        return key1 == null ? key2 == null : key1.equals(key2);
    }

    private boolean shouldResize() {
        return size >= buckets.length * DEFAULT_LOAD_FACTOR;
    }

    private void resize() {
        LinkedList<Node<K, V>>[] oldBuckets = buckets;
        buckets = new LinkedList[buckets.length * 2];
        size = 0;

        for (LinkedList<Node<K, V>> bucket : oldBuckets) {
            if (bucket != null) {
                for (Node<K, V> node : bucket) {
                    put(node.key, node.value);
                }
            }
        }
    }
}
