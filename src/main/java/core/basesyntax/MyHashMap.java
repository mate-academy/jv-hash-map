package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static double LOAD_FACTOR = 0.75;

    private Node<K, V>[] table;
    private int size;
    private int currentCapacity;
    private double threshold;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        currentCapacity = DEFAULT_CAPACITY;
        threshold = DEFAULT_CAPACITY * LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> currentNode = table[getBucket(key)];
        Node<K, V> newNode = new Node<>(key, value, null);

        if (currentNode == null) {
            size++;
            resizeIfNeeded();
            table[getBucket(key)] = newNode;
            return;
        }
        if (Objects.equals(key, currentNode.key)) {
            newNode.next = currentNode.next;
            table[getBucket(key)] = newNode;
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
        resizeIfNeeded();
        currentNode.next = newNode;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getBucket(key)];
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

    private int getBucket(K key) {
        if (key == null) {
            return 0;
        }
        return key.hashCode() % currentCapacity;
    }

    private void resizeIfNeeded() {
        if (size > threshold) {
            currentCapacity *= 2;
            threshold = currentCapacity * LOAD_FACTOR;
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
