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
        int index = hash(key) % table.length;
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        addNode(index, key, value);
    }

    public V getValue(K key) {
        if (key == null) {
            return getNullKey();
        }
        int index = hash(key) % table.length;
        Node<K, V> node = getKeyNode(key, index);
        return node != null ? node.value : null;
    }

    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private void addNode(int index, K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resizeTable();
        }
        Node<K, V> node = new Node<>(key, value);
        node.next = table[index];
        table[index] = node;
        size++;
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

    private Node<K, V> getNullKeyNode() {
        Node<K, V> node = table[0];
        while (node != null) {
            if (node.key == null) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private Node<K, V> getKeyNode(K key, int index) {
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private V getNullKey() {
        Node<K, V> node = getNullKeyNode();
        return node != null ? node.value : null;
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
