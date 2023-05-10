package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int GROW_FACTOR = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node [DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        putValue(key, value, table);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = table[findIndex(key, table.length)];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
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

    private int findIndex(K key, int length) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % length);
    }

    private void putValue(K key, V value, Node<K, V>[] currentTable) {
        if (currentTable[findIndex(key, currentTable.length)] == null) {
            currentTable[findIndex(key, currentTable.length)] = new Node<>(key, value, null);
        } else {
            addNewNode(currentTable[findIndex(key, currentTable.length)], key, value);
        }
    }

    private void addNewNode(Node<K, V> currentNode, K key, V value) {
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                size--;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(key, value, null);
                return;
            }
            currentNode = currentNode.next;
        }
    }

    private void resize() {
        int newCapacity = table.length * GROW_FACTOR;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (Node<K, V> node : table) {
            if (node != null) {
                while (node != null) {
                    putValue(node.key, node.value, newTable);
                    node = node.next;
                }
            }
        }
        table = newTable;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
    }

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
