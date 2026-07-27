package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int MAP_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int capacity;
    private double loadFactor;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.capacity = MAP_CAPACITY;
        this.loadFactor = LOAD_FACTOR;
        this.table = (Node<K, V>[]) new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value);
            size++;
        } else {
            Node<K, V> current = table[index];
            while (current != null) {
                if (Objects.equals(key, current.key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = new Node<>(key, value);
                    size++;
                    resize();
                    return;
                }
                current = current.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(key, current.key)) {
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

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        if ((double) size / capacity > loadFactor) {
            Node<K, V>[] oldTable = table;
            table = new Node[capacity * 2];
            capacity = capacity * 2;
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    Node<K, V> next = node.next;
                    node.next = null;
                    addNode(node, table);
                    node = next;
                }
            }
        }
    }

    private void addNode(Node<K, V> node, Node<K, V>[] targetTable) {
        int index = hash(node.key) % targetTable.length;
        if (targetTable[index] == null) {
            targetTable[index] = node;
        } else {
            Node<K, V> current = targetTable[index];
            while (current.next != null) {
                current = current.next;
            }
            current.next = node;
        }
    }
}
