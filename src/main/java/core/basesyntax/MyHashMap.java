package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K,V> implements MyMap<K,V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        Node<K,V> bucket = table[getBucketIndex(hash(key))];
        Node<K,V> newNode = new Node<>(key, value, null);
        if (bucket != null) {
            while (bucket != null) {
                if (Objects.equals(bucket.key, key)) {
                    bucket.value = value;
                    return;
                }
                bucket = bucket.next;
            }
            bucket = table[Math.abs(hash(key)) % table.length];
            while (bucket.next != null) {
                bucket = bucket.next;
            }
            bucket.next = newNode;
            if (++size > threshold) {
                resize();
            }
            return;
        }
        table[Math.abs(hash(key)) % table.length] = newNode;
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> bucket = table[getBucketIndex(hash(key))];
        if (bucket != null) {
            while (bucket != null) {
                if (Objects.equals(bucket.key, key)) {
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
        size = 0;
        Node<K, V>[] temp = table;
        table = (Node<K, V>[]) new Node[table.length << 1];
        threshold = (int) (table.length * LOAD_FACTOR);
        for (Node<K, V> bucket : temp) {
            while (bucket != null) {
                put(bucket.key, bucket.value);
                bucket = bucket.next;
            }
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private int getBucketIndex(int hash) {
        return Math.abs(hash) % table.length;
    }

    private static class Node<K,V> {
        private V value;
        private K key;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
