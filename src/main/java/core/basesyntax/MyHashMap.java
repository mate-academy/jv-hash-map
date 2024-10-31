package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int capacity;
    private Node<K, V> nullKeyNode; // Special bucket for null keys

    public MyHashMap() {
        this.capacity = DEFAULT_INITIAL_CAPACITY;
        this.table = (Node<K, V>[]) new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (size >= capacity * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        if (key == null) {
            putForNullKey(value);
            return;
        }
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        Node<K, V> newNode = new Node<>(key.hashCode(), key, value, table[index]);
        table[index] = newNode;
        size++;
    }

    private void putForNullKey(V value) {
        if (nullKeyNode == null) {
            nullKeyNode = new Node<>(0, null, value, null);
            size++;
        } else {
            nullKeyNode.value = value;
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return nullKeyNode == null ? null : nullKeyNode.value;
        }
        Node<K, V> node = getNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = capacity * 2;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        for (Node<K, V> currentNode : table) {
            Node<K, V> node = currentNode;
            while (node != null) {
                Node<K, V> next = node.next;
                int index = (node.key == null ? 0 : node.hash) & (newCapacity - 1);
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
        capacity = newCapacity;
    }

    private int getIndex(K key) {
        return key == null ? 0 : (key.hashCode() & (capacity - 1));
    }

    private Node<K, V> getNode(K key) {
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}

