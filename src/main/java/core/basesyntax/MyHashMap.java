package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private Node<K, V>[] table;
    private int size;

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == DEFAULT_CAPACITY * LOAD_FACTOR) {
            resize();
        }
        int index = getBucket(key, table.length);
        Node<K, V> node = findNode(key, index);
        if (node != null) {
            node.value = value;
        } else {
            Node<K, V> entry = new Node<>(key, value, table[index]);
            table[index] = entry;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = findNode(key, getBucket(key, table.length));
        return node != null ? node.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> findNode(K key, int index) {
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private int getBucket(K key, int capacity) {
        return key == null ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] current = table;
        table = new Node[table.length * 2];
        for (Node<K, V> bucket : current) {
            while (bucket != null) {
                put(bucket.key, bucket.value);
                bucket = bucket.next;
            }
        }
    }
}

