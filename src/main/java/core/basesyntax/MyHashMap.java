package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        this.table = (Node<K,V>[]) new Node[DEFAULT_CAPACITY];
        this.threshold = (int)(LOAD_FACTOR * DEFAULT_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        Node<K, V> bucket = table[bucketIndex];
        if (bucket == null) {
            table[bucketIndex] = new Node<>(key, value);
        } else {
            while (bucket.next != null) {
                if (Objects.equals(bucket.key, key)) {
                    bucket.value = value;
                    return;
                }
                bucket = bucket.next;
            }
            if (Objects.equals(bucket.key, key)) {
                bucket.value = value;
                return;
            }
            bucket.next = new Node<>(key, value);
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> bucket = table[getBucketIndex(key)];
        while (bucket != null) {
            if (Objects.equals(bucket.key, key)) {
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

    private void resize() {
        size = 0;
        int newCapacity = table.length << 1;
        Node<K, V>[] oldTable = table;
        table = (Node<K,V>[]) new Node[newCapacity];
        threshold = (int)(LOAD_FACTOR * newCapacity);
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int hash(K key) {
        int result;
        return (key == null) ? 0 : (result = key.hashCode()) ^ (result >>> 16);
    }

    private int getBucketIndex(K key) {
        return Math.abs(hash(key) % table.length);
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
