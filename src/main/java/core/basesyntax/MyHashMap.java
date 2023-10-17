package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_MULTIPLICATION = 2;
    private Node<K, V>[] buckets;
    private int size;
    private int capacity;
    private float threshold;

    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.threshold = DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR;
        this.buckets = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = getIndex(key, capacity);
        Node<K, V> node = buckets[index];
        Node<K, V> newNode = new Node<>(key, value);
        if (node == null) {
            buckets[index] = newNode;
        } else {
            Node<K, V> prev = node;
            while (node != null) {
                if (Objects.equals(key, node.key)) {
                    node.value = value;
                    return;
                }
                prev = node;
                node = node.next;
            }
            prev.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key, capacity);
        Node<K, V> node = buckets[index];
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

    private int getIndex(K key, int capacity) {
        return hash(key) % capacity;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private void resize() {
        int newCapacity = capacity * DEFAULT_MULTIPLICATION;
        Node<K, V>[] newBucket = new Node[newCapacity];
        for (int i = 0; i < capacity; i++) {
            Node<K, V> node = buckets[i];
            while (node != null) {
                Node<K, V> next = node.next;
                int index = getIndex(node.key, newCapacity);
                node.next = newBucket[index];
                newBucket[index] = node;
                node = next;
            }
        }
        buckets = newBucket;
        capacity = newCapacity;
        threshold = capacity * DEFAULT_LOAD_FACTOR;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
