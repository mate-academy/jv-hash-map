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
            table[index] = new Node<>(key, value);
            size++;
            return;
        } else {
            Node<K, V> tableNode = getNodeByKey(key);
            if (tableNode != null) {
                tableNode.value = value;
                return;
            }
        }
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = table[index];
        table[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> tableNode = getNodeByKey(key);
        if (tableNode == null) {
            return null;
        }
        return tableNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> getNodeByKey(K key) {
        Node<K, V> tableNode = table[getIndex(key)];
        while (tableNode != null && !Objects.equals(tableNode.key, key)) {
            tableNode = tableNode.next;
        }
        return tableNode;
    }

    private void resize() {
        Node<K, V>[] newTable = new Node[table.length * GROWTH_FACTOR];
        threshold = (int) (newTable.length * DEFAULT_LOAD_FACTOR);
        this.size = DEFAULT_SIZE;
        transferNodesToBiggerMap(table);
    }

    private void transferNodesToBiggerMap(Node<K, V>[] oldTable) {
        this.table = new Node[table.length * GROWTH_FACTOR];
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
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
