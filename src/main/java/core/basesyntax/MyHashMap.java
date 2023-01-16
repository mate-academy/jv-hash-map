package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;
    private final double loadFactor;

    public MyHashMap(int capacity, double loadFactor) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Illegal capacity: " + capacity);
        }
        if (loadFactor <= 0) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }
        int initialCapacity = getInitialCapacity(capacity);
        this.loadFactor = loadFactor;
        table = new Node[initialCapacity];
    }

    public MyHashMap() {
        this(DEFAULT_CAPACITY, LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= loadFactor * table.length) {
            resize();
        }
        int index = getIndex(hash(key));
        if (table[index] == null) {
            table[index] = new Node<>(hash(key), key, value, null);
            size++;
            return;
        }
        Node<K, V> tempNode = table[index];
        while (tempNode.next != null && !Objects.equals(tempNode.key, key)) {
            tempNode = tempNode.next;
        }
        if (Objects.equals(tempNode.key, key)) {
            tempNode.value = value;
            return;
        }
        tempNode.next = new Node<>(hash(key), key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(hash(key));
        Node<K, V> tempNode = table[index];
        while (tempNode != null) {
            if (Objects.equals(tempNode.key, key)) {
                return tempNode.value;
            }
            tempNode = tempNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[table.length << 1];
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> temp = oldTable[i];
            while (temp != null) {
                Node<K, V> tempNext = temp.next;
                temp.next = null;
                moveToNewTable(temp);
                temp = tempNext;
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int getIndex(int hash) {
        return hash & (table.length - 1);
    }

    private void moveToNewTable(Node<K, V> node) {
        int index = getIndex(node.hash);
        if (table[index] == null) {
            table[index] = node;
            return;
        }
        Node<K, V> tempNode = table[index];
        while (tempNode.next != null) {
            tempNode = tempNode.next;
        }
        tempNode.next = node;
    }

    private int getInitialCapacity(int capacity) {
        int initialCapacity = Integer.highestOneBit(capacity);
        return initialCapacity == capacity ? capacity : initialCapacity << 1;
    }

    private class Node<K, V> {
        private int hash;
        private K key;
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
