package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node(Objects.hash(key), key, value, null);
        int indexFor = hash(node.key);
        if (table[indexFor] == null) {
            add(indexFor, node);
        } else {
            putVal(node);
        }
        int n = size;
        if (++n > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int indexFor = hash(key);
        Node<K, V> currentBucket = table[indexFor];
        while (currentBucket != null) {
            if (Objects.equals(currentBucket.key, key)) {
                return currentBucket.value;
            }
            currentBucket = currentBucket.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private void putVal(Node<K, V> node) {
        int indexFor = hash(node.key);
        Node<K, V> currentBucket = table[indexFor];
        while (currentBucket != null) {
            if (Objects.equals(node.key, currentBucket.key)) {
                currentBucket.value = node.value;
                return;
            }
            if (currentBucket.next == null) {
                currentBucket.next = node;
                size++;
            }
            currentBucket = currentBucket.next;
        }
    }

    private void resize() {
        final Node<K, V>[] old = table;
        table = new Node[newCapacity()];
        threshold = (int) (table.length * LOAD_FACTOR);
        size = 0;
        transfer(old);
    }

    private void transfer(Node<K, V>[] old) {
        for (Node<K, V> bucket : old) {
            while (bucket != null) {
                put(bucket.key, bucket.value);
                bucket = bucket.next;
            }
        }
    }

    private int newCapacity() {
        return table.length << 1;
    }

    private void add(int index, Node<K, V> node) {
        table[index] = node;
        size++;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }
}
