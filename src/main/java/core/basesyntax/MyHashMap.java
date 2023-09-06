package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int MAX_CAPACITY = Integer.MAX_VALUE;
    private static final int ARRAY_INCREASE = 2;
    private Node<K, V>[] table = new Node[INITIAL_CAPACITY];
    private int size;

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value, null);
        int position = getPosition(key);
        if (size >= (int) (LOAD_FACTOR * table.length)) {
            table = resize();
            position = getPosition(key);
        }
        if (table[position] == null) {
            table[position] = newNode;
            size++;
        } else {
            Node<K, V> node = table[position];
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

    @Override
    public V getValue(K key) {
        int position = getPosition(key);
        Node<K, V> node = table[position];
        while (node != null) {
            if (Objects.deepEquals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V>[] resize() {
        if (table.length >= MAX_CAPACITY) {
            throw new IllegalStateException("Maximum capacity reached");
        }
        int newCapacity = Math.min(table.length * ARRAY_INCREASE, MAX_CAPACITY);
        Node<K, V>[] newTable = new Node[newCapacity];
        for (Node<K, V> node : table) {
            Node<K, V> currentNode = node;
            while (currentNode != null) {
                int position = getPosition(currentNode.key, newCapacity);
                Node<K, V> newNode = newTable[position];
                if (newNode == null) {
                    newTable[position] = new Node<>(currentNode.key, currentNode.value, null);
                } else {
                    newNode.next = new Node<>(currentNode.key, currentNode.value, newNode.next);
                }
                currentNode = currentNode.next;
            }
        }
        return newTable;
    }

    private int getPosition(K key) {
        return getPosition(key, table.length);
    }

    private int getPosition(K key, int capacity) {
        return Math.abs(key == null ? 0 : key.hashCode() % capacity);
    }

    private class Node<K, V> {
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
