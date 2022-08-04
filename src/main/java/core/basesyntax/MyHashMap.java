package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int currentCapacity;
    private int size;
    private int threshold;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
        this.currentCapacity = DEFAULT_CAPACITY;
        this.threshold = (int)(DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        putValue(getBucketIndexForKey(key), key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = getBucketIndexForKey(key);
        Node<K, V> currentNode = table[hash];
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

    private void resize() {
        if (size >= threshold) {
            Node<K, V>[] oldTable = table;
            currentCapacity = currentCapacity << 1;
            threshold = (int)(currentCapacity * DEFAULT_LOAD_FACTOR);
            table = new Node[currentCapacity];
            transfer(oldTable);
        }
    }

    private void transfer(Node<K, V>[] oldTable) {
        for (Node<K, V> node : oldTable) {
            Node<K, V> currentNode = node;
            while (currentNode != null) {
                putValue(getBucketIndexForKey(currentNode.key), currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private void putValue(int hash, K key, V value) {
        if (table[hash] == null) {
            table[hash] = new Node<>(hash, key, value);
        } else {
            Node<K, V> currentNode = table[hash];
            while (currentNode.next != null) {
                if (Objects.equals(key, currentNode.key)) {
                    break;
                }
                currentNode = currentNode.next;
            }
            if (Objects.equals(key, currentNode.key)) {
                currentNode.value = value;
                size--;
            } else {
                currentNode.next = new Node<>(hash, key, value);
            }
        }
    }

    private int getBucketIndexForKey(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % currentCapacity;
    }


    private class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value) {
            this.key = key;
            this.value = value;
            this.hash = hash;
        }
    }
}
