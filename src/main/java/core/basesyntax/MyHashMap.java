package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int DEFAULT_MULTIPLIER = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (LOAD_FACTOR * DEFAULT_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        if (table[getHashIndexByKey(key)] == null) {
            putOnEmpty(key, value);
        } else {
            insertValue(key, value);
        }
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getHashIndexByKey(key)];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.nextNode;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHashIndexByKey(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void putOnEmpty(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value, null);
        table[getHashIndexByKey(key)] = newNode;
        size++;
    }

    private void insertValue(K key, V value) {
        Node<K, V> currentNode = table[getHashIndexByKey(key)];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.nextNode == null) {
                currentNode.nextNode = new Node<>(key, value, null);
                size++;
                return;
            }
            currentNode = currentNode.nextNode;
        }
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * DEFAULT_MULTIPLIER];
        threshold = (int) (table.length * LOAD_FACTOR);
        for (Node<K, V> currentNode : oldTable) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.nextNode;
            }
        }

    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> nextNode;

        public Node(K key, V value, Node<K, V> nextNode) {
            this.key = key;
            this.value = value;
            this.nextNode = nextNode;
        }
    }
}
