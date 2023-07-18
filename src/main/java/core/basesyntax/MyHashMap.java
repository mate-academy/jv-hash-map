package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPCITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int GROWTH_COEFICIENT = 2;
    private Node<K,V>[] table;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPCITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> tempNode = getNode(key);
        if (tempNode == null) {
            growTableIfNeeded();
            addNewNode(key, value);
            size++;
        } else {
            tempNode.value = value;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> tempNode = getNode(key);
        return tempNode != null ? tempNode.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getBucket(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % table.length);
    }

    private Node<K,V> getNode(K key) {
        int bucket = getBucket(key);
        Node<K,V> currentNode = table[bucket];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private void growTableIfNeeded() {
        if (size < table.length * LOAD_FACTOR) {
            return;
        }
        Node<K,V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[table.length * GROWTH_COEFICIENT];
        for (Node<K,V> node : oldTable) {
            while (node != null) {
                addNewNode(node.key, node.value);
                node = node.next;
            }
        }
    }

    private void addNewNode(K key, V value) {
        int bucket = getBucket(key);
        Node<K, V> newNode = new Node<>(key, value);
        Node<K, V> currentNode = table[bucket];
        if (currentNode == null) {
            table[bucket] = newNode;
            return;
        }
        while (currentNode.next != null) {
            currentNode = currentNode.next;
        }
        currentNode.next = newNode;
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
