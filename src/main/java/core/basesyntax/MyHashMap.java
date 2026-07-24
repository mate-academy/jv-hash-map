package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int CAPACITY_MULTIPLIER = 2;

    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = createTable(DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key, table.length);
        Node<K, V> currentNode = table[index];

        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }

            currentNode = currentNode.next;
        }

        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = table[index];
        table[index] = newNode;
        size++;

        if (size > table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key, table.length);
        Node<K, V> currentNode = table[index];

        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }

            currentNode = currentNode.next;
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = createTable(oldTable.length * CAPACITY_MULTIPLIER);

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                Node<K, V> nextNode = node.next;

                int newIndex = getIndex(node.key, table.length);
                node.next = table[newIndex];
                table[newIndex] = node;

                node = nextNode;
            }
        }
    }

    private int getIndex(K key, int capacity) {
        if (key == null) {
            return 0;
        }

        return (key.hashCode() & Integer.MAX_VALUE) % capacity;
    }

    @SuppressWarnings("unchecked")
    private Node<K, V>[] createTable(int capacity) {
        return (Node<K, V>[]) new Node[capacity];
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
