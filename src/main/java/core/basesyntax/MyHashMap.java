package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int capacity;
    private float threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
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
        } else {
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
        }
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
        capacity *= 2;
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            Node<K, V> current = node;
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
        threshold = (int) (capacity * LOAD_FACTOR);
    }

    private int getBucketIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % capacity;
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

