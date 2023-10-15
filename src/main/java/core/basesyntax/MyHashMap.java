package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int DEFAULT_GROW_FACTOR = 2;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int capacity;
    private int size;
    private int threshold;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
        this.capacity = DEFAULT_CAPACITY;
        this.threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = getIndexByKey(key, capacity);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = table[index];
        table[index] = newNode;
        size++;
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = searchNodeByKey(key);
        return (node != null) ? node.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = capacity * DEFAULT_GROW_FACTOR;
        Node<K, V>[] newTable = new Node[newCapacity];
        int index;
        for (Node<K, V> node : table) {
            while (node != null) {
                index = getIndexByKey(node.key, newCapacity);
                Node<K, V> next = node.next;
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
        capacity = newCapacity;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
    }

    private int getIndexByKey(K key, int capacity) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % capacity);
    }

    private Node<K, V> searchNodeByKey(K key) {
        int index = getIndexByKey(key, capacity);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if ((key == null && currentNode.key == null) || (key != null && key.equals(
                    currentNode.key))) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return null;
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
