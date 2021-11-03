package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPASITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPASITY];
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

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node(key, value, null);
        int i = getIndex(key);

        if (size == getLoadLimit()) {
            resize(table);
        }
        if (table[i] == null) {
            table[i] = newNode;
            size++;
        } else {
            Node<K, V> node = table[i];
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = newNode;
                    size++;
                    return;
                }
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndex(key)];

        while (node != null) {
            if (Objects.equals(node.key, key)) {
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

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private int getLoadLimit() {
        return (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    private void resize(Node<K, V>[] table) {
        size = 0;
        int newLength = table.length * 2;
        migrateTable(table, newLength);
    }

    private void migrateTable(Node<K, V>[] oldTable, int length) {
        this.table = new Node[length];

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
