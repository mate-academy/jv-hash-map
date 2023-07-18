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
        size = 0;
    }

    public MyHashMap() {
        this(DEFAULT_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resizeTable();
        }

        int index = (key == null) ? 0 : getIndex(key, table.length);
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
        int index = (key == null) ? 0 : getIndex(key, table.length);
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

    private int getIndex(K key, int capacity) {
        return (key == null) ? 0 : (key.hashCode() & (capacity - 1));
    }

    private void resizeTable() {
        int newCapacity = table.length * RESIZING_CONSTANT;
        Node<K, V>[] newTable = new Node[newCapacity];

        for (Node<K, V> node : table) {
            Node<K, V> current = node;
            while (current != null) {
                int index = getIndex(current.key, newCapacity);
                Node<K, V> newNode = new Node<>(current.key, current.value, null);
                if (newTable[index] == null) {
                    newTable[index] = newNode;
                } else {
                    Node<K, V> newCurrent = newTable[index];
                    while (newCurrent.next != null) {
                        newCurrent = newCurrent.next;
                    }
                    newCurrent.next = newNode;
                }
                current = current.next;
            }
        }

        table = newTable;
    }

    class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
