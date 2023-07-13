package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_FACTOR = 2;
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K, V>[] table;
    private int size;
    private int capacity;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void put(K key, V value) {
        checkSize();
        int index = getIndex(key);
        if (table[index] == null) {
            addNewNode(key, value);
            return;
        }
        Node<K, V> currentNode = table[index];
        if (Objects.equals(key, currentNode.key)) {
            changeValue(currentNode, value);
            return;
        }
        while (!Objects.equals(key, currentNode.key)) {
            if (currentNode.next == null) {
                currentNode.next = new Node<>(key, value);
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
        changeValue(currentNode, value);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getIndex(key)];
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

    private void checkSize() {
        if (size == capacity * LOAD_FACTOR) {
            resize();
        }
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % capacity;
    }

    private void resize() {
        capacity *= RESIZE_FACTOR;
        Node<K, V>[] tmp = table;
        table = (Node<K, V>[]) new Node[capacity];
        size = 0;
        for (Node<K, V> node : tmp) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private void addNewNode(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value);
        table[getIndex(key)] = newNode;
        size++;
    }

    private void changeValue(Node<K, V> node, V value) {
        node.value = value;
    }
}
