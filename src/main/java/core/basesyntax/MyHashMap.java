package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int GROW_INDEX = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int capacity;
    private int size;
    private int threshold;

    public MyHashMap() {
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
        this.capacity = DEFAULT_INITIAL_CAPACITY;
        this.threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        if (++size > threshold) {
            resize();
        }
        putValue(getBucketIndex(key), key, value);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getBucketIndex(key)];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
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

    private void resize() {
        capacity = capacity * GROW_INDEX;
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                putValue(getBucketIndex(node.key), node.key, node.value);
                node = node.next;
            }
        }
    }

    private void putValue(int bucketIndex, K key, V value) {
        if (table[bucketIndex] == null) {
            table[bucketIndex] = new Node<>(bucketIndex, key, value);
            return;
        }
        Node<K, V> node = table[bucketIndex];
        while (!(node.next == null || Objects.equals(key, node.key))) {
            node = node.next;
        }
        if (Objects.equals(key, node.key)) {
            node.value = value;
            size--;
            return;
        }
        node.next = new Node<>(bucketIndex, key, value);
    }

    private int getBucketIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private static class Node<K, V> {
        private int bucketIndex;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int bucketIndex, K key, V value) {
            this.key = key;
            this.value = value;
            this.bucketIndex = bucketIndex;
        }
    }
}
