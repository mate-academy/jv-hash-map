package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private static final int CAPACITY_MULTIPLIER = 2;
    private int threshold;
    private int capacity;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        threshold = (int) (capacity * LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            table = resize();
        }
        setNode(new Node<>(key, value, null));
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getHash(key) % capacity];
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

    private void setNode(Node<K, V> newNode) {
        int indexOfBucket = getHash(newNode.key) % capacity;
        if (table[indexOfBucket] == null) {
            table[indexOfBucket] = newNode;
            size++;
            return;
        }
        Node<K, V> currentNode = table[indexOfBucket];
        while (currentNode != null) {
            if (Objects.equals(newNode.key, currentNode.key)) {
                currentNode.value = newNode.value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = newNode;
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
    }

    private Node<K, V>[] resize() {
        capacity = capacity * CAPACITY_MULTIPLIER;
        threshold = (int) (capacity * LOAD_FACTOR);
        Node<K, V>[] oldMap = table;
        table = (Node<K, V>[]) new Node[capacity];
        size = 0;
        for (Node<K, V> currentNode : oldMap) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
        return table;
    }

    private int getHash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode());
    }

    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
