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
        if (size > threshold) {
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

    private class Node<K, V> {
        private Node<K, V> next;
        private int hash;
        private K key;
        private V value;

        public Node(int hash, K key, V value) {
            this.key = key;
            this.value = value;
            this.hash = hash;
        }
    }

    private void putValue(int hash, K key, V value) {
        if (hashTable[hash] == null) {
            hashTable[hash] = new Node<>(hash, key, value);
            return;
        }
        Node<K, V> currentNode = hashTable[hash];
        while (currentNode.next != null) {
            if (Objects.equals(key, currentNode.key)) {
                break;
            }
            currentNode = currentNode.next;
        }
        if (Objects.equals(key, currentNode.key)) {
            currentNode.value = value;
            size--;
            return;
        }
        currentNode.next = new Node<>(hash, key, value);
    }

    private void fill(Node<K, V>[] oldTable) {
        for (Node<K, V> node : oldTable) {
            Node<K, V> currentNode = node;
            while (currentNode != null) {
                putValue(getIndexOfBucket(currentNode.key), currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private void resize() {
        if (size >= threshold) {
            capacity *= 2;
            threshold = capacity * DEFAULT_LOAD_FACTOR;
            Node<K, V>[] oldTable = hashTable;
            hashTable = new Node[capacity];
            fill(oldTable);
        }
    }

    private int getIndexOfBucket(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % capacity;
    }
}
