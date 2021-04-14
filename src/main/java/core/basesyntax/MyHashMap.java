package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    private static class Node<K, V> {
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

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = hash(key);
        if (table[index] == null) {
            addNewNode(key, value, index);
        } else {
            attachNode(key, value, index);
        }
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void addNewNode(K key, V value, int index) {
        Node<K, V> newNode = new Node<>(index, key, value, null);
        table[index] = newNode;
        size++;
    }

    private void attachNode(K key, V value, int index) {
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                Node<K, V> newNode = new Node<>(index, key, value, null);
                currentNode.next = newNode;
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int newCapacity = oldTable.length * 2;
        threshold = (int) (newCapacity * LOAD_FACTOR);
        table = new Node[newCapacity];
        size = 0;

        transferOfNodes(oldTable);
    }

    private void transferOfNodes(Node<K, V>[] oldTable) {
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> currentNode = oldTable[i];
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node<K, V> currentNode = table[index];
        return searchNode(currentNode, key);
    }

    private V searchNode(Node<K, V> currentNode, K key) {
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
}
