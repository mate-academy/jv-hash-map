package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold;
    private Node<K, V>[] table;
    private int size;
    private int capacity;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
        capacity = DEFAULT_CAPACITY;
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> currentNode = table[getIndex(key)];
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

    private boolean resizeIfNeeded() {
        if (size <= threshold) {
            return false;
        }
        final int oldCapacity = capacity;
        capacity *= 2;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] oldTab = table;
        table = (Node<K, V>[]) new Node[capacity];
        size = 0;
        Node<K, V> currentNode;
        for (int i = 0; i < oldCapacity; i++) {
            currentNode = oldTab[i];
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
        return true;
    }

    static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % capacity);
    }
}
