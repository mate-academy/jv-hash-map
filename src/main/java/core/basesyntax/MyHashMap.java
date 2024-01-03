package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int BASE_CAPACITY = 16;
    private int capacity;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        capacity = BASE_CAPACITY;
        threshold = (int) (capacity * LOAD_FACTOR);
        size = 0;
        table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if ((size + 1) > threshold) {
            resize();
        }
        if (putNode(key, value, table, capacity)) {
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int hashKey = key == null ? 0 : Math.abs(key.hashCode());
        Node<K, V> currentNode = table[hashKey % capacity];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.nextNode;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        capacity = capacity << 1;
        threshold = threshold << 1;

        Node<K, V>[] newTable = new Node[capacity];

        for (Node<K, V> node : table) {
            while (node != null) {
                putNode(node.key, node.value, newTable, capacity);
                node = node.nextNode;
            }
        }
        table = newTable;
    }

    private boolean putNode(K key, V value, Node<K, V>[] currentTable, int capacity) {
        Node<K, V> node = new Node<>(key, value, null);
        Node<K, V> currentNode = currentTable[node.hash % capacity];

        if (currentNode == null) {
            currentTable[node.hash % capacity] = node;
            return true;
        }

        while (currentNode.nextNode != null && !Objects.equals(node.key, currentNode.key)) {
            currentNode = currentNode.nextNode;
        }

        if (Objects.equals(node.key, currentNode.key)) {
            currentNode.value = node.value;
            return false;
        }
        currentNode.nextNode = node;
        return true;
    }

    private static class Node<K, V> {
        private final K key;
        private final int hash;
        private V value;
        private Node<K, V> nextNode;

        public Node(K key, V value, Node<K, V> nextNode) {
            this.key = key;
            this.value = value;
            this.nextNode = nextNode;
            this.hash = key == null ? 0 : Math.abs(key.hashCode());
        }
    }
}
