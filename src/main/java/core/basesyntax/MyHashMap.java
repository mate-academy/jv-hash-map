package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K, V>[] table;
    private int size;
    private double loadFactor = 0.75;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > table.length * loadFactor) {
            resize();
        }
        Node<K, V> entry = new Node<>(key, value, null);
        Node<K, V> existing = table[getHash(key)];
        if (existing == null) {
            table[getHash(key)] = entry;
            size++;
        } else {
            putSupplier(key, value, (Node<K, V>) entry, (Node<K, V>) existing);
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> bucket = table[getHash(key)];
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

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        size = 0;
        Node<K,V>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        for (Node<K,V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private void putSupplier(K key, V value, Node<K, V> entry, Node<K, V> existing) {
        while (existing.next != null) {
            if (Objects.equals(existing.key, key)) {
                existing.value = value;
                return;
            }
            existing = existing.next;
        }
        if (Objects.equals(existing.key, key)) {
            existing.value = value;
        } else {
            existing.next = entry;
            size++;
        }
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
