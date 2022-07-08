package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private Node<K,V>[] buckets;

    public MyHashMap() {
        buckets = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == buckets.length * LOAD_FACTOR) {
            resize();
        }
        int index = findIndex(key);
        int hashKey = key == null ? 0 : Math.abs(key.hashCode());
        Node<K, V> bucket = buckets[index];
        while (bucket != null) {
            if (Objects.equals(key, bucket.key)) {
                bucket.value = value;
                return;
            }
            if (bucket.next == null) {
                size++;
                bucket.next = new Node<>(key, value, hashKey, null);
                return;
            }
            bucket = bucket.next;
        }
        size++;
        buckets[index] = new Node<>(key,value, hashKey, null);
    }

    @Override
    public V getValue(K key) {
        if (buckets[findIndex(key)] != null) {
            Node<K, V> bucket = buckets[findIndex(key)];
            while (bucket != null) {
                if (Objects.equals(key, bucket.key)) {
                    return bucket.value;
                }
                bucket = bucket.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] newBuckets = new Node[buckets.length * 2];
        for (Node<K, V> bucket: buckets) {
            while (bucket != null) {
                int index = bucket.hash % newBuckets.length;
                if (newBuckets[index] == null) {
                    newBuckets[index] = new Node<>(bucket.key, bucket.value, bucket.hash, null);
                } else {
                    Node<K,V> newBucket = newBuckets[index];
                    while (newBucket != null) {
                        if (newBucket.next == null) {
                            newBucket.next =
                                    new Node<>(bucket.key, bucket.value, bucket.hash, null);
                            break;
                        }
                        newBucket = newBucket.next;
                    }
                }
                bucket = bucket.next;
            }
        }
        buckets = newBuckets;
    }

    private int findIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % buckets.length);
    }

    private static class Node<K,V> {
        private V value;
        private K key;
        private int hash;
        private Node<K,V> next;

        Node(K key, V value, int hash, Node<K, V> next) {
            this.value = value;
            this.key = key;
            this.hash = hash;
            this.next = next;
        }
    }
}
