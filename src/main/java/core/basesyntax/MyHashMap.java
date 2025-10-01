package core.basesyntax;

import java.util.Arrays;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int MAXIMUM_CAPACITY = 2131233211;
    private static final int RESIZE_FACTOR = 2;
    private static final int HASH_MASK = 0x7FFFFFFF;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        this.threshold = (int)(DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = hash(key) % table.length;
        Node<K, V> current = table[index];
        if (current == null) {
            table[index] = new Node<>(key, value);
            size++;
            return;
        }
        while (true) {
            if ((key == null && current.key == null)
                    || (key != null && key.equals(current.key))) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                current.next = new Node<>(key, value);
                size++;
                return;
            }
            current = current.next;
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key) % table.length;
        Node<K, V> current = table[index];
        while (current != null) {
            if ((key == null && current.key == null)
                    || (key != null && key.equals(current.key))) {
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

    @Override
    public String toString() {
        return "MyHashMap{"
                + "table=" + Arrays.toString(table)
                + ", size=" + size
                + ", threshold=" + threshold
                + '}';
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode() & HASH_MASK;
    }

    private void resize() {
        int oldCapacity = table.length;
        int newCapacity;
        if (oldCapacity >= MAXIMUM_CAPACITY) {
            newCapacity = MAXIMUM_CAPACITY;
        } else {
            long expanded = (long) oldCapacity * RESIZE_FACTOR;
            newCapacity = (int) Math.min(expanded, MAXIMUM_CAPACITY);
        }
        @SuppressWarnings("unchecked")
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        transfer(table, newTable);
        table = newTable;
        threshold = (int)(newCapacity * LOAD_FACTOR);
    }

    private void transfer(Node<K, V>[] oldTable, Node<K, V>[] newTable) {
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                Node<K, V> next = node.next;
                int newIndex = hash(node.key) % newTable.length;
                node.next = newTable[newIndex];
                newTable[newIndex] = node;
                node = next;
            }
        }
    }

    private static class Node<K, V> {
        private Node<K, V> next;
        private final K key;
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }
}
