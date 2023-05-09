package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int indexOfBucket = getIndexOfBucket(key);
        Node<K, V> newNode = new Node<>(key, value);
        if (table[indexOfBucket] == null) {
            table[indexOfBucket] = newNode;
            size++;
        } else {
            putNotEmptyBucket(table[indexOfBucket], newNode);
        }
    }

    @Override
    public V getValue(K key) {
        int indexOfBucket = getIndexOfBucket(key);
        Node<K, V> entry = table[indexOfBucket];
        while (entry != null) {
            if (Objects.equals(entry.key, key)) {
                return entry.value;
            }
            entry = entry.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putNotEmptyBucket(Node<K, V> existEntry, Node<K, V> newEntry) {
        while (existEntry != null) {
            if (Objects.equals(existEntry.key, newEntry.key)) {
                existEntry.value = newEntry.value;
                return;
            }
            if (existEntry.next == null) {
                existEntry.next = newEntry;
                size++;
                return;
            }
            existEntry = existEntry.next;
        }
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length << 1];
        threshold = (int) (DEFAULT_LOAD_FACTOR * table.length);
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                K key = node.key;
                V value = node.value;
                put(key, value);
                node = node.next;
            }
        }
    }

    private int getIndexOfBucket(K key) {
        if (key == null) {
            return 0;
        }
        return getHash(key) % table.length;
    }

    private int getHash(K key) {
        return Math.abs(key == null ? 0 : key.hashCode());
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
