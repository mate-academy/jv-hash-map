package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int INCREASE_VALUE = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }

        int nodeIndex = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value);

        if (table[nodeIndex] == null) {
            table[nodeIndex] = newNode;
        } else {
            Node<K, V> node = table[nodeIndex];

            while (node.next != null) {
                if (isKeyDuplicate(node, key)) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }

            if (isKeyDuplicate(node, key)) {
                node.value = value;
                return;
            }

            node.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> node : table) {
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    return node.value;
                }
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private void resize() {
        Node<K, V>[] newTable = new Node[table.length * INCREASE_VALUE];
        threshold = (int) (newTable.length * LOAD_FACTOR);
        Node<K, V>[] copingTable = table;
        table = newTable;
        size = 0;

        for (Node<K, V> node : copingTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getKeyHash(K key) {
        if (key == null) {
            return 0;
        }
        return key.hashCode();
    }

    private int getIndex(K key) {
        return Math.abs(getKeyHash(key) % table.length);
    }

    private boolean isKeyDuplicate(Node<K, V> node, K key) {
        return (node.key == null && key == null) || (node.key != null && node.key.equals(key));
    }
}
