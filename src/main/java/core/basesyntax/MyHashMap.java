package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<K, V>[] table;
    private int size;
    private int currentCapacity;
    private int threshold;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        currentCapacity = DEFAULT_CAPACITY;
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> currentNode = table[getIndex(key)];
        Node<K, V> newNode = new Node<>(key, value);

        if (currentNode == null) {
            size++;
            resizeIfNeeded();
            table[getIndex(key)] = newNode;
            return;
        }
        if (Objects.equals(key, currentNode.key)) {
            newNode.next = currentNode.next;
            table[getIndex(key)] = newNode;
            return;
        }
        while (currentNode.next != null) {
            if (Objects.equals(key, currentNode.next.key)) {
                newNode.next = currentNode.next.next;
                currentNode.next = newNode;
                return;
            }
            currentNode = currentNode.next;
        }
        size++;
        if (resizeIfNeeded()) {
            put(newNode.key, newNode.value);
        }
        currentNode.next = newNode;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getIndex(key)];
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

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % currentCapacity;
    }

    private boolean resizeIfNeeded() {
        if (size <= threshold) {
            return false;
        }
        final int oldCapacity = currentCapacity;
        currentCapacity *= 2;
        threshold = (int) (currentCapacity * LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[currentCapacity];
        size = 0;

        Node<K, V> currentNode;
        for (int i = 0; i < oldCapacity; i++) {
            currentNode = oldTable[i];
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
        return true;
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
