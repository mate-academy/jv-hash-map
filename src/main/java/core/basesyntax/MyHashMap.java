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

    private class Node<K, V>{
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {

        Node<K, V> newNode = new Node(getHash(key), key, value, null);

        int i = getIndex(key);

        if (size == getLoadLimit()) {
            resize(table);
        }

        if (table[i] == null) {
            table[i] = newNode;
            size++;
        } else {
            Node<K, V> nodeByIndex = table[i];
            while (nodeByIndex != null) {
                if (Objects.equals(nodeByIndex.key, key)) {
                    nodeByIndex.value = value;
                    return;
                }
                if (nodeByIndex.next == null) {
                    nodeByIndex.next = newNode;
                    size++;
                    return;
                }
                nodeByIndex = nodeByIndex.next;
            }
        }
    }

    @Override
    public V getValue(K key) {

        Node<K, V> node = table[getIndex(key)];

        while (node != null) {
            if (Objects.equals(node.key, key))
                return node.value;
            node = node.next;
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHash(K key) {
        return key == null ? 0 : key.hashCode();
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
