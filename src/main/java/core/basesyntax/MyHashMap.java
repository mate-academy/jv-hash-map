package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_TABLE_LENGTH = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int INCREASING_TABLE = 2;
    private Node<K, V> [] table;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_TABLE_LENGTH];
    }

    @Override
    public void put(K key, V value) {
        checkSize();
        Node<K, V> insertNode = new Node<>(key, value);
        int index = getIndex(key);
        Node<K, V> node = table[index];
        if (table[index] == null) {
            table[index] = insertNode;
            size++;
            return;
        }
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = insertNode;
                size++;
                return;
            }
            node = node.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(key);
        return node != null ? node.value : null;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void checkSize() {
        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
    }

    private Node<K, V> getNode(K key) {
        int index = getIndex(key);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (Objects.equals(node.key, key)) {
                return node;
            }
        }
        return null;
    }

    private void resize() {
        int newTableLength = table.length * INCREASING_TABLE;
        Node<K, V>[] oldTable = table;
        table = new Node[newTableLength];
        size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> node = oldTable[i];
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
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
