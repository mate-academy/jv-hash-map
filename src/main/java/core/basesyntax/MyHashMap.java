package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    public void put(K key, V value) {
        checkCapacity();
        int position = getIndex(key);
        if (table[position] == null) {
            table[position] = new Node<>(key, value);
            size++;
            return;
        }
        Node<K, V> current = table[position];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
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

    public V getValue(K key) {
        Node<K, V> node = getNodeByKey(key);
        return node != null ? node.value : null;
    }

    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private void checkCapacity() {
        if (size >= table.length * LOAD_FACTOR) {
            resizeTable();
        }
    }

    private int getIndex(K key) {
        return hash(key) % table.length;
    }

    private void resizeTable() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (Node<K, V> node : table) {
            while (node != null) {
                int index = hash(node.key) % newCapacity;
                Node<K, V> next = node.next;
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
    }

    private Node<K, V> getNodeByKey(K key) {
        Node<K, V> node = table[getIndex(key)];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
