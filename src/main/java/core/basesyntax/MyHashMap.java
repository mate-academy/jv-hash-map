package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private double loadFactor;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap(int capacity, double loadFactor) {
        this.loadFactor = loadFactor;
        this.table = new Node[capacity];
    }

    public MyHashMap() {
        loadFactor = LOAD_FACTOR;
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = table[index];
        table[index] = newNode;
        if (++size > table.length * loadFactor) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public void remove(K key) {
        int index = getIndex(key);
        Node<K, V> node = table[index];
        Node<K, V> prev = null;
        while (node != null) {
            if (node.key.equals(key)) {
                if (prev == null) {
                    table[index] = node.next;
                } else {
                    prev.next = node.next;
                }
                size--;
                return;
            }
            prev = node;
            node = node.next;
        }
    }

    private void resize() {
        int newCapacity = table.length * 2;
        size = 0;
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[newCapacity];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
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
