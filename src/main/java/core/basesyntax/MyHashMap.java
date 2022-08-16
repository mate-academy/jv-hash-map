package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private float threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = DEFAULT_CAPACITY * LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            increaseCapacity();
        }
        int index = getBucketIndex(key);
        Node<K, V> node = table[index];

        if (node == null) {
            table[index] = new Node<>(key, value);
            size++;
            return;
        }
        while (node.next != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        if (Objects.equals(node.key, key)) {
            node.value = value;
            return;
        }
        node.next = new Node<>(key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getBucketIndex(key);
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
    public int getSize() {
        return size;
    }

    private void increaseCapacity() {
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * 2];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        threshold = table.length * LOAD_FACTOR;
    }

    private int getBucketIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
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

