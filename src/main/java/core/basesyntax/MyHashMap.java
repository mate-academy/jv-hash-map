package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int POSITIVE_MASK = Integer.MAX_VALUE;
    private Node<K, V>[] table = new Node[DEFAULT_CAPACITY];
    private final int lengthExtensions = table.length * 2;

    private int size = 0;

    @Override
    public void put(K key, V value) {
        if (size >= table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }

        int index = getIndex(key, table.length);
        Node<K, V> currentNode = table[index];

        if (currentNode == null) {
            table[index] = new Node<>(key, value);
            size++;
            return;
        }

        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }

            if (currentNode.next == null) {
                currentNode.next = new Node<>(key, value);
                size++;
                return;
            }

            currentNode = currentNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key, table.length);
        Node<K, V> currentNode = table[index];

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

    private void resize() {
        int newCapacity = lengthExtensions;
        Node<K, V>[] newTable = new Node[newCapacity];

        for (int i = 0; i < table.length; i++) {
            Node<K, V> currentNode = table[i];

            while (currentNode != null) {
                Node<K, V> nextNode = currentNode.next;
                int newIndex = getIndex(currentNode.key, newCapacity);

                currentNode.next = newTable[newIndex];
                newTable[newIndex] = currentNode;
                currentNode = nextNode;
            }
        }

        table = newTable;
    }

    private int getIndex(K key, int capacity) {
        return (Objects.hashCode(key) & POSITIVE_MASK) % capacity;
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
