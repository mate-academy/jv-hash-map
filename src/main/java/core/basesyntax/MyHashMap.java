package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_CAPACITY = 0.75f;
    private static final int RESIZE_VALUE = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        threshold = (int)(DEFAULT_CAPACITY * DEFAULT_LOAD_CAPACITY);
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }

        Node<K, V> entry = table[getIndex(key)];

        while (entry != null) {
            if (Objects.equals(entry.key, key)) {
                entry.value = value;
                return;
            }
            if (entry.next == null) {
                entry.next = new Node<K, V>(key, value);
                size++;
                return;
            }
            entry = entry.next;
        }
        table[getIndex(key)] = new Node<>(key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> entry = table[getIndex(key)];

        if (table == null) {
            return null;
        }

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

    public void resize() {
        int newCapacity = table.length * RESIZE_VALUE;
        threshold *= RESIZE_VALUE;
        Node<K, V>[] oldTable = table;
        table = new Node[newCapacity];
        size = 0;
        for (Node<K, V> entry : oldTable) {
            while (entry != null) {
                put(entry.key, entry.value);
                entry = entry.next;
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
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
