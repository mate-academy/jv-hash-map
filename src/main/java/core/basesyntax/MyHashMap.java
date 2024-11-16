package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private final float loadFactor;
    private final int scaleFactor;
    private int size;
    private int capacity;
    private Node<K, V> [] table;

    public MyHashMap() {
        this(16, .75f);
    }

    public MyHashMap(int initialCapacity, float loadFactor) {
        scaleFactor = 2;
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
        capacity = initialCapacity;
        if (loadFactor < 0 || loadFactor > 1) {
            throw new IllegalArgumentException("Illegal Load: " + loadFactor);
        }
        table = (Node<K, V>[]) new Node[initialCapacity];
        this.loadFactor = loadFactor;
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
        Node<K, V> newNode = new Node<>(key, value);
        int index = calculateIndex(newNode.key);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
            return;
        }
        Node<K, V> currentNode = table[index];
        Node<K, V> previousNode = currentNode;
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            } else {
                previousNode = currentNode;
                currentNode = currentNode.next;
            }
        }
        previousNode.next = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = calculateIndex(key);
        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(key, current.key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resizeIfNeeded() {
        if (size == capacity * loadFactor) {
            capacity *= scaleFactor;
            size = 0;
            Node<K, V>[] oldTable = table;
            table = (Node<K, V>[])new Node[capacity];
            for (Node<K, V> node : oldTable) {
                if (node != null) {
                    put(node.key, node.value);
                    if (node.next != null) {
                        Node<K, V> nodeToTransfer = node;
                        while (nodeToTransfer.next != null) {
                            put(nodeToTransfer.next.key, nodeToTransfer.next.value);
                            nodeToTransfer = nodeToTransfer.next;
                        }
                    }
                }
            }
        }
    }

    private int calculateIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;

        }
    }
}
