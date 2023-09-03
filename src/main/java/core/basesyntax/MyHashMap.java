package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_MULTIPLIER = 2;
    private int size;
    private int capacity;
    private float loadFactor;
    private Node<K,V>[] buckets;

    public MyHashMap() {
        this(INITIAL_CAPACITY, LOAD_FACTOR);
    }

    public MyHashMap(int capacity, float loadFactor) {
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        this.buckets = new Node[capacity];
    }

    public void put(K key, V value) {
        if (size > capacity * loadFactor) {
            resize();
        }

        int index = hash(key);
        Node<K,V> node = buckets[index];

        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }

        Node<K,V> newNode = new Node<>(key,value);
        newNode.next = buckets[index];
        buckets[index] = newNode;
        size++;
    }

    public V getValue(K key) {
        int index = hash(key);
        Node<K,V> node = buckets[index];

        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }

        return null;
    }

    public int getSize() {
        return size;
    }

    private int hash(K key) {
        int hash = key == null ? 0 : key.hashCode() % capacity;
        return hash < 0 ? hash * -1 : hash;
    }

    private void resize() {
        capacity = capacity * CAPACITY_MULTIPLIER;
        Node<K,V> [] newBuckets = new Node[capacity];

        for (int i = 0; i < capacity / CAPACITY_MULTIPLIER; i++) {
            Node<K,V> node = buckets[i];

            while (node != null) {
                int index = hash(node.key);
                var currentNode = newBuckets[index];

                if (currentNode == null) {
                    newBuckets[index] = new Node<>(node.key, node.value);
                } else {
                    while (currentNode.next != null) {
                        currentNode = currentNode.next;
                    }
                    currentNode.next = new Node<>(node.key, node.value);
                }

                node = node.next;
            }
        }

        buckets = newBuckets;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
