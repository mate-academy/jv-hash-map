package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private Node[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int numberOfBucket = getBucket(key);
        checkSizeOfTable();
        Node<K, V> newNode = new Node<>(null, key, value);
        if (table[numberOfBucket] == null) {
            table[numberOfBucket] = newNode;
        } else {
            Node<K, V> currentNode = table[numberOfBucket];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, newNode.key)) {
                    currentNode.value = newNode.value;
                    return;
                }
                if (currentNode.next == null) {
                    break;
                }
                currentNode = currentNode.next;
            }
            currentNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> firstNode = table[getBucket(key)];
        while (firstNode != null) {
            if (Objects.equals(firstNode.key, key)) {
                return firstNode.value;
            }
            firstNode = firstNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void checkSizeOfTable() {
        if (size == (int) (table.length * DEFAULT_LOAD_FACTOR)) {
            grow();
        }
    }

    private void grow() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getBucket(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % table.length);
    }

    private class Node<K, V> {
        private Node<K, V> next;
        private K key;
        private V value;

        public Node(Node<K, V> next, K key, V value) {
            this.next = next;
            this.key = key;
            this.value = value;
        }
    }
}
