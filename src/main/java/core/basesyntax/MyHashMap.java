package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPASITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int GROWTH_FACTOR = 2;
    private static final int DEFAULT_SIZE = 0;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPASITY];
        threshold = (int) (DEFAULT_CAPASITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int index = getIndex(key);
        if (table[index] == null) {
            Node<K, V> newNode = new Node<>(key, value);
            table[index] = newNode;
            size++;
            return;
        } else {
            Node<K, V> tableNode = table[index];
            while (tableNode != null) {
                if (Objects.equals(tableNode.key, key)) {
                    tableNode.value = value;
                    return;
                }
                tableNode = tableNode.next;
            }
        }
        Node<K, V> oldNode = table[index];
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = oldNode;
        table[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> tableNode = table[getIndex(key)];
        while (tableNode != null && !Objects.equals(tableNode.key, key)) {
            tableNode = tableNode.next;
        }
        if (tableNode == null) {
            return null;
        }
        return tableNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int capasity = table.length * GROWTH_FACTOR;
        Node<K, V>[] newTable = new Node[capasity];
        threshold = (int) (newTable.length * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        this.table = newTable;
        this.size = DEFAULT_SIZE;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private final int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private int hash;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
