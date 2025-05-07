package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int RESIZING_CONSTANT = 2;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap(int capacity) {
        table = new Node[capacity];
    }

    public MyHashMap() {
        this(DEFAULT_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resizeTable();
        }

        int index = (key == null) ? 0 : getIndex(key);
        Node<K, V> newNode = new Node<>(key, value, null);

        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> current = table[index];
            Node<K, V> prev = null;
            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                prev = current;
                current = current.next;
            }
            prev.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = (key == null) ? 0 : getIndex(key);
        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resizeTable() {
        int newCapacity = table.length * RESIZING_CONSTANT;
        MyHashMap<K, V> newHashMap = new MyHashMap<>(newCapacity);

        for (Node<K, V> node : table) {
            Node<K, V> current = node;
            while (current != null) {
                newHashMap.put(current.key, current.value);
                current = current.next;
            }
        }

        table = newHashMap.table;
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
