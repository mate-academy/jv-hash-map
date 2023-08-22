package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] tables;
    private int threshold;
    private int size;

    public MyHashMap() {
        tables = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        updateThreshold();
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        int bucketIndex = getBucketIndex(key);
        if (tables[bucketIndex] == null) {
            tables[bucketIndex] = newNode;
            size++;
        } else {
            linkNode(tables[bucketIndex], newNode);
        }
        if (size >= threshold) {
            resizeTables();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = tables[getBucketIndex(key)];
        while (current != null) {
            if (current.equalsByKey(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return Math.abs(Objects.hashCode(key));
    }

    private int getBucketIndex(K key) {
        return hash(key) % tables.length;
    }

    private void linkNode(Node<K, V> current, Node<K, V> newNode) {
        while (true) {
            if (current.equalsByKey(newNode.key)) {
                current.value = newNode.value;
                return;
            }
            if (!current.hasNext()) {
                current.next = newNode;
                size++;
                return;
            }
            current = current.next;
        }
    }

    private void resizeTables() {
        final Node<K, V>[] temp = tables;
        tables = (Node<K, V>[]) new Node[tables.length << 1];
        size = 0;
        updateThreshold();
        for (Node<K, V> node : temp) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private void updateThreshold() {
        threshold = (int) (LOAD_FACTOR * tables.length);
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> node) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = node;
        }

        private boolean equalsByKey(K another) {
            return Objects.equals(key, another);
        }

        private boolean hasNext() {
            return next != null;
        }
    }
}
