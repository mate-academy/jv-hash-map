package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int MAX_CAPACITY = Integer.MAX_VALUE;
    private Node<K, V>[] table = new Node[INITIAL_CAPACITY];
    private int size;
    private int threshold;
    private int capacity;

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key == null ? 0
                : key.hashCode(), key, value, null);
        capacity = table.length;
        int position = getPosition(key, capacity);
        threshold = (int) (LOAD_FACTOR * capacity);

        if (size >= threshold) {
            table = resize();
        }

        for (int i = 0; i < capacity; i++) {
            if (table[i] != null && Math.abs(table[i].key == null ? 0
                    : table[i].hash % capacity) == position) {
                Node<K, V> node = table[i];
                while (node != null) {
                    if (Objects.deepEquals(node.key, key)) {
                        node.value = value;
                        return;

                    } else if (node.next == null) {
                        node.next = newNode;
                        size++;
                        return;
                    }
                    node = node.next;
                }
            }
        }

        table[position] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        if (capacity == 0) {
            return null;
        }
        int position = getPosition(key, capacity);
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null && i == position) {
                Node<K, V> node = table[i];
                for (int j = 0; j <= size; j++) {
                    if (node.key == null && key == null || Objects.equals(node.key, key)) {
                        return node.value;
                    } else {
                        if (node.next != null) {
                            node = node.next;
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V>[] resize() {
        if (threshold == MAX_CAPACITY) {
            return table;
        }

        int newCapacity = capacity * 2;
        Node<K, V>[] newTable = new Node[newCapacity];
        threshold = (int) (LOAD_FACTOR * newCapacity);

        for (int i = 0; i < table.length; i++) {
            Node<K, V> currentNode = table[i];
            while (currentNode != null) {
                int position = getPosition(currentNode.key, newCapacity);
                Node<K, V> newNode = newTable[position];
                if (newNode == null) {
                    newTable[position] = new Node<>(currentNode.hash,
                            currentNode.key, currentNode.value, null);
                } else {
                    newNode.next = new Node<>(currentNode.hash,
                            currentNode.key, currentNode.value, newNode.next);
                }
                currentNode = currentNode.next;
            }
        }
        return newTable;
    }

    private int getPosition(K key, int capacity) {
        return Math.abs(key == null ? 0
                : key.hashCode() % capacity);
    }

    private class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
