package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int RESIZE_VALUE = 2;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
        Node<K, V> entry = new Node<>(key, value, null);
        int index = getIndex(key);
        Node<K, V> existing = table[index];
        if (existing == null) {
            table[index] = entry;
            size++;
            return;
        }
        while (existing.next != null) {
            if (Objects.equals(existing.key, key)) {
                existing.value = value;
                return;
            }
            existing = existing.next;
        }
        if (Objects.equals(existing.key, key)) {
            existing.value = value;
            return;
        }
        existing.next = entry;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> bucket = table[getIndex(key)];
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

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        size = 0;
        Node<K,V>[] oldTable = table;
        table = new Node[oldTable.length * RESIZE_VALUE];
        for (Node<K,V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
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
