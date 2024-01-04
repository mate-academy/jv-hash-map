package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int BASE_CAPACITY = 16;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        size = 0;
        table = new Node[BASE_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if ((size + 1) > table.length * LOAD_FACTOR) {
            resize();
        }
        if (putNode(key, value, table)) {
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int hashKey = key == null ? 0 : Math.abs(key.hashCode());
        Node<K, V> currentNode = table[hashKey % table.length];

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
        int newCapacity = table.length << 1;

        Node<K, V>[] newTable = new Node[newCapacity];

        for (Node<K, V> node : table) {
            while (node != null) {
                putNode(node.key, node.value, newTable);
                node = node.nextNode;
            }
        }
        table = newTable;
    }

    private boolean putNode(K key, V value, Node<K, V>[] currentTable) {
        int capacity = currentTable.length;
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
