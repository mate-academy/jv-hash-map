package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int CAPACITY_INCREASE_FACTOR = 2;
    private float loadFactor;
    private int capacity;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        capacity = DEFAULT_INITIAL_CAPACITY;
        loadFactor = DEFAULT_LOAD_FACTOR;
        table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (capacity * DEFAULT_LOAD_FACTOR < size) {
            resize();
        }
        int index = calculateIndex(key, capacity);
        Node<K, V> node = getNodeByKey(key);
        if (node != null) {
            node.value = value;
        } else {
            Node<K, V> newNode = new Node<>(key, value);
            if (table[index] != null) {
                newNode.next = table[index];
            }
            table[index] = newNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNodeByKey(key);
        return node != null ? node.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> getNodeByKey(K key) {
        int index = calculateIndex(key, capacity);
        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    private int calculateIndex(K key, int capacity) {
        return key != null ? (key.hashCode() & (capacity - 1)) : 0;
    }

    private void resize() {
        int newCapacity = capacity * CAPACITY_INCREASE_FACTOR;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (int i = 0; i < capacity; i++) {
            Node<K, V> current = table[i];
            while (current != null) {
                Node<K, V> next = current.next;
                int newIndex = calculateIndex(current.key, newCapacity);
                current.next = newTable[newIndex];
                newTable[newIndex] = current;
                current = next;
            }
        }
        table = newTable;
        capacity = newCapacity;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
