package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (table.length * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resizeTables();
        }

        Node<K, V> newNode = new Node<>(key, value, null);
        int bucketIndex = getBucketIndex(key);

        if (table[bucketIndex] == null) {
            table[bucketIndex] = newNode;
            size++;
        } else {
            linkNode(table[bucketIndex], newNode);
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = table[getBucketIndex(key)];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
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
        int hash = Objects.hashCode(key);

        if (hash < 0) {
            hash *= -1;
        }

        return hash;
    }

    private int getBucketIndex(K key) {
        return hash(key) % table.length;
    }

    private void linkNode(Node<K, V> current, Node<K, V> newNode) {
        for (Node<K, V> start = current; start != null; start = start.next) {
            if (Objects.equals(start.key, newNode.key)) {
                start.value = newNode.value;
                return;
            }
            current = start;
        }

        current.next = newNode;
        size++;
    }

    private void resizeTables() {
        final Node<K, V>[] temp = table;
        table = (Node<K, V>[]) new Node[table.length * 2];
        size = 0;
        threshold = (int) (LOAD_FACTOR * table.length);

        for (Node<K, V> node : temp) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> node) {
            this.key = key;
            this.value = value;
            this.next = node;
        }
    }
}
