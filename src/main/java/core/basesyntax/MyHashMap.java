package core.basesyntax;

import java.util.LinkedList;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private LinkedList<Node<K, V>>[] buckets;
    private int size;
    private int threshold;

    public MyHashMap() {
        buckets = new LinkedList[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }

        int bucketIndex = getBucketIndex(key);
        LinkedList<Node<K, V>> bucket = buckets[bucketIndex];

        if (bucket == null) {
            bucket = new LinkedList<>();
            buckets[bucketIndex] = bucket;
        }

        if (key == null) {
            for (Node<K, V> node : bucket) {
                if (node.key == null) {
                    node.value = value;
                    return;
                }
            }
            bucket.add(new Node<>(null, value));
        } else {
            for (Node<K, V> node : bucket) {
                if (key.equals(node.key)) {
                    node.value = value;
                    return;
                }
            }
            bucket.add(new Node<>(key, value));
        }

        size++;
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getBucketIndex(key);
        LinkedList<Node<K, V>> bucket = buckets[bucketIndex];

        if (bucket != null) {
            if (key == null) {
                for (Node<K, V> node : bucket) {
                    if (node.key == null) {
                        return node.value;
                    }
                }
            } else {
                for (Node<K, V> node : bucket) {
                    if (key.equals(node.key)) {
                        return node.value;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getBucketIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % buckets.length);
    }

    private void resize() {
        LinkedList<Node<K, V>>[] oldBuckets = buckets;
        buckets = new LinkedList[oldBuckets.length * 2];
        threshold = (int) (buckets.length * LOAD_FACTOR);
        size = 0;

        for (LinkedList<Node<K, V>> bucket : oldBuckets) {
            if (bucket != null) {
                for (Node<K, V> node : bucket) {
                    put(node.key, node.value);
                }
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
