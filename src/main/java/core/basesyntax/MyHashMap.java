package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == 0) {
            initializeTable();
        }
        Node<K, V> newNode = new Node<>(key, value);
        int index = getIndex(key);
        Node<K, V> current = table[index];
        if (current == null) {
            table[index] = newNode;
            checkThreshold();
        }
        while (current != null) {
            if (Objects.equals(key, current.key)) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                current.next = newNode;
                checkThreshold();
            }
            current = current.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = getNode(key);
        return current == null ? null : current.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private Node<K, V> getNode(K key) {
        Node<K, V> current = table[getIndex(key)];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    private void initializeTable() {
        table = new Node[table.length << 1];
        threshold = (int) (table.length * LOAD_FACTOR);
    }

    private void checkThreshold() {
        if (++size > threshold) {
            resizeTable();
        }
    }

    private void resizeTable() {
        Node<K, V>[] oldTable = table;
        size = 0;
        transfer(oldTable);
    }

    private void transfer(Node<K, V>[] oldTable) {
        for (Node<K, V> bucket : oldTable) {
            while (bucket != null) {
                put(bucket.key, bucket.value);
                bucket = bucket.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
