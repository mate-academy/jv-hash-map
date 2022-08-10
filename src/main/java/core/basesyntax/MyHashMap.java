package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int currentCapacity;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.currentCapacity = DEFAULT_INITIAL_CAPACITY;
        this.threshold = (int) (DEFAULT_INITIAL_CAPACITY * LOAD_FACTOR);
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        putValue(getBucketIndex(key), key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = getBucketIndex(key);
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

    private void transfer(Node<K, V>[] oldTable) {
        for (Node<K, V> node : oldTable) {
            Node<K,V> currentNode = node;
            while (currentNode != null) {
                putValue(getBucketIndex(currentNode.key), currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private void resize() {
        if (size >= threshold) {
            currentCapacity = currentCapacity * 2;
            threshold = (int) (currentCapacity * DEFAULT_INITIAL_CAPACITY);
            Node<K, V>[] oldTable = table;
            table = new Node[currentCapacity];
            transfer(oldTable);
        }
    }

    private int getBucketIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % currentCapacity);
    }

    private void putValue(int hash, K key, V value) {
        if (table[hash] == null) {
            table[hash] = new Node<>(key, value, hash);
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
                currentNode.next = new Node<>(key, value, hash);
            }
        }
    }

    private final class Node<K, V> {
        private K key;
        private V value;
        private int hash;
        private Node<K, V> next;

        public Node(K key, V value, int hash) {
            this.key = key;
            this.value = value;
            this.hash = hash;
        }
    }
}
