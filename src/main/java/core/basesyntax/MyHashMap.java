package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int GROWTH_FACTOR = 2;
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
        putElement(key, value, buckets);
        size++;
    }

    @Override
    public V getValue(K key) {
        if (buckets[findIndex(key, buckets.length)] != null) {
            Node<K, V> bucket = buckets[findIndex(key, buckets.length)];
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
        Node<K, V>[] newBuckets = new Node[buckets.length * GROWTH_FACTOR];
        for (Node<K, V> bucket: buckets) {
            while (bucket != null) {
                putElement(bucket.key, bucket.value, newBuckets);
                bucket = bucket.next;
            }
        }
        buckets = newBuckets;
    }

    private void putElement(K key, V value, Node<K, V>[] buckets) {
        int index = findIndex(key, buckets.length);
        Node<K, V> bucket = buckets[index];
        while (bucket != null) {
            if (Objects.equals(key, bucket.key)) {
                bucket.value = value;
                size--;
                return;
            }
            if (bucket.next == null) {
                bucket.next = new Node<>(key, value, null);
                return;
            }
            bucket = bucket.next;
        }
        buckets[index] = new Node<>(key, value,null);
    }

    private int findIndex(K key, int length) {
        return key == null ? 0 : Math.abs(key.hashCode() % length);
    }

    private static class Node<K,V> {
        private V value;
        private final K key;
        private Node<K,V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.value = value;
            this.key = key;
            this.next = next;
        }
    }
}
