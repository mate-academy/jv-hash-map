package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int capacity = INITIAL_CAPACITY;
    private int size;
    private float threshold = capacity * LOAD_FACTOR;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = (Node<K,V>[])new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = getIndex(key);
        Node<K,V> currentNode = table[index];
        Node<K, V> lastNode = null;

        while (currentNode != null) {
            lastNode = currentNode;
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        if (lastNode == null) {
            table[index] = newNode;
        } else {
            lastNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K,V> currentNode = table[index];
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

    private int getIndex(K key) {
        int h = hash(key) == Integer.MIN_VALUE ? 0 : Math.abs(hash(key));
        return h % capacity;
    }

    private int hash(K key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private void resize() {
        capacity = capacity * RESIZE_MULTIPLIER;
        threshold = capacity * LOAD_FACTOR;
        transfer();
    }

    @SuppressWarnings("unchecked")
    private void transfer() {
        Node<K, V>[] newSizeTable = (Node<K, V>[])new Node[capacity];
        for (int i = 0; i < table.length; i++) {
            Node<K, V> currentNode = table[i];
            while (currentNode != null) {
                Node<K, V> nextNode = currentNode.next;
                currentNode.next = null;

                int indexForNewTable = getIndex(currentNode.key);
                insertNodeAtNewTable(currentNode, newSizeTable, indexForNewTable);

                currentNode = nextNode;
            }
        }
        table = newSizeTable;
    }

    private void insertNodeAtNewTable(Node<K, V> node,
                                      Node<K, V>[] newTable, int indexForNewTable) {
        if (newTable[indexForNewTable] == null) {
            newTable[indexForNewTable] = node;
            return;
        }

        Node<K, V> currentNode = newTable[indexForNewTable];
        while (currentNode.next != null) {
            currentNode = currentNode.next;
        }
        currentNode.next = node;
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
