package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int GROW_FACTOR = 2;
    private Node<K, V>[] table;
    private int size;
    private int capacity;
    private double threshold;

    public MyHashMap() {
        capacity = INITIAL_CAPACITY;
        threshold = capacity * DEFAULT_LOAD_FACTOR;
        table = (Node<K, V>[]) new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int hash = hash(key);
        int bucketIndex = getIndex(key);
        Node<K, V> node = table[bucketIndex];
        while (node != null) {
            if (node.hash == hash && Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        Node<K, V> newNode = new Node<>(hash, key, value, table[bucketIndex]);
        table[bucketIndex] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int bucketIndex = getIndex(key);
        Node<K, V> node = table[bucketIndex];
        while (node != null) {
            if (node.hash == hash && Objects.equals(node.key, key)) {
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

    private int hash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(hash(key) % capacity);
    }

    private void resize() {
        capacity = capacity * GROW_FACTOR;
        threshold = capacity * DEFAULT_LOAD_FACTOR;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[capacity];
        for (Node<K, V> node : table) {
            while (node != null) {
                Node<K, V> next = node.next;
                int bucketIndex = getIndex(node.key);
                node.next = newTable[bucketIndex];
                newTable[bucketIndex] = node;
                node = next;
            }
        }
        table = newTable;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
