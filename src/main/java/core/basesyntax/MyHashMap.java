package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private int capacity;
    private Node<K, V>[] table;

    public MyHashMap() {
    }

    @Override
    public void put(K key, V value) {
        table = resize();
        int bucket = getBucket(key);
        Node<K, V> node = table[bucket];
        Node<K, V> newNode;
        if (node == null) {
            table[bucket] = new Node<>(key, value, null);
            size++;
            return;
        }
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            } else if (node.next == null) {
                newNode = new Node<>(key, value, null);
                node.next = newNode;
                size++;
                return;
            }
            node = node.next;
        }
    }

    @Override
    public V getValue(K key) {
        table = resize();
        int bucket = getBucket(key);
        Node<K, V> node = table[bucket];
        if (node == null) {
            return null;
        }
        while (node != null) {
            if (Objects.equals(node.key, key)) {
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

    private Node<K, V>[] resize() {
        if (threshold == 0) {
            threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
            capacity = DEFAULT_INITIAL_CAPACITY;
            return new Node[capacity];
        }
        if (threshold > size) {
            return table;
        }
        size = 0;
        capacity = capacity << 1;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                this.put(node.key, node.value);
                node = node.next;
            }
        }
        return table;
    }

    private int getBucket(K key) {
        int hash = (key == null) ? 0 : key.hashCode();
        return Math.abs(hash) % capacity;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
