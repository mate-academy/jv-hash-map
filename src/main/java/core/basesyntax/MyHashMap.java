package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_MULTIPLIER = 2;
    private int size;
    private Node<K, V>[] table;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int hash = (key == null) ? 0 : key.hashCode();
        addEntry(hash, key, value);
        if (size > threshold) {
            resizeTable();
        }
    }

    @Override
    public V getValue(K key) {
        int hash = (key == null) ? 0 : key.hashCode();
        int bucketIndex = getIndexByHash(hash, table.length);
        Node<K, V> currentNode = table[bucketIndex];
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

    private void addEntry(int hash, K key, V value) {
        Node<K, V> newNode = new Node<>(hash, key, value, null);
        int bucketIndex = getIndexByHash(hash, table.length);
        Node<K, V> currentNode = table[bucketIndex];
        Node<K, V> previousNode = null;
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            previousNode = currentNode;
            currentNode = currentNode.next;
        }
        if (previousNode == null) {
            table[bucketIndex] = newNode;
        } else {
            previousNode.next = newNode;
        }
        size++;
    }

    private void resizeTable() {
        int newCapacity = table.length * CAPACITY_MULTIPLIER;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] newTable = new Node[newCapacity];
        for (Node<K, V> currentNode: table) {
            while (currentNode != null) {
                Node<K, V> nextNode = currentNode.next;
                int index = getIndexByHash(currentNode.hash, newCapacity);
                currentNode.next = newTable[index];
                newTable[index] = currentNode;
                currentNode = nextNode;
            }
        }
        table = newTable;
    }

    private int getIndexByHash(int hash, int tableLength) {
        return Math.abs(hash) % tableLength;
    }

    private class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
