package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K, V>[] hashTable;
    private int size;
    private int capacity;
    private float threshold;

    public MyHashMap() {
        hashTable = new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
        threshold = DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        putValue(getIndexOfBucket(key), key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = getIndexOfBucket(key);
        Node<K, V> currentNode = hashTable[hash];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
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

    private void putValue(int hash, K key, V value) {
        Node<K, V> newNode = new Node<>(key, value);
        if (hashTable[hash] == null) {
            hashTable[hash] = newNode;
            return;
        }
        Node<K, V> currentNode = hashTable[hash];
        while (currentNode.next != null && !Objects.equals(key, currentNode.key)) {
            currentNode = currentNode.next;
        }
        if (Objects.equals(key, currentNode.key)) {
            currentNode.value = value;
            size--;
            return;
        }
        currentNode.next = newNode;
    }

    private void resize() {
        capacity *= 2;
        threshold = capacity * DEFAULT_LOAD_FACTOR;
        Node<K, V>[] oldHashTable = hashTable;
        hashTable = new Node[capacity];
        for (Node<K, V> node : oldHashTable) {
            while (node != null) {
                putValue(getIndexOfBucket(node.key), node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndexOfBucket(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private static class Node<K, V> {
        private Node<K, V> next;
        private K key;
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
