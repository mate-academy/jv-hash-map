package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int MULTIPLAYER_CAPACITY = 2;
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        double thresholdResize = (double) size / table.length;
        if (thresholdResize > LOAD_FACTOR) {
            resize();
        }
        putNode(table, key, value);
    }

    @Override
    public V getValue(K key) {
        int index = getHashIndex(table, key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHashIndex(Node<K, V>[] table, K key) {
        int index;
        if (key == null) {
            return 0;
        } else {
            index = Math.abs(key.hashCode() % table.length);
        }
        return index;
    }

    private void resize() {
        final int currentSize = size;
        int newCapacity = table.length * MULTIPLAYER_CAPACITY;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (Node<K, V> node : table) {
            while (node != null) {
                putNode(newTable, node.key, node.value);
                node = node.next;
            }
        }
        size = currentSize;
        table = newTable;
    }

    private void putNode(Node<K, V>[] table, K key, V value) {
        int index = getHashIndex(table, key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value);
        }
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(key, node.key) && node.next != null) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = table[index];
        table[index] = newNode;
        size++;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
