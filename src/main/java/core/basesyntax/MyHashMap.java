package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private int capacity;

    public MyHashMap() {
        tableInitialization();
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        int cellToPut = (capacity - 1) & getHash(key);
        Node<K, V> newNode = new Node<>(key, value, null);

        putValue(table[cellToPut], newNode, cellToPut);

        if (size > threshold) {
            resizeTable();
        }
    }

    @Override
    public V getValue(K key) {
        int cell = (capacity - 1) & getHash(key);
        Node<K, V> currentNode = table[cell];
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

    private void tableInitialization() {
        table = (Node<K, V> []) new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int)(DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        capacity = DEFAULT_INITIAL_CAPACITY;
    }

    private void putValue(Node<K, V> currentNode, Node<K, V> newNode, int cellToPut) {
        if (currentNode == null) {
            table[cellToPut] = newNode;
            size++;
            return;
        }
        do {
            if (Objects.equals(currentNode.key, newNode.key)) {
                currentNode.value = newNode.value;
                break;
            }
            if (currentNode.next == null) {
                currentNode.next = newNode;
                size++;
            }
            currentNode = currentNode.next;
        } while (true);
    }

    private void resizeTable() {
        final Node<K, V>[] oldTable = table;
        capacity *= 2;
        table = (Node<K, V> [])new Node[capacity];
        threshold = (int)(capacity * DEFAULT_LOAD_FACTOR);
        size = 0;
        refill(oldTable);
    }

    private void refill(Node<K, V>[] nodes) {
        for (Node<K, V> node : nodes) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getHash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }
}
