package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_MULTIPLIER = 2;
    private int size;
    private Node<K,V>[] buckets;

    public MyHashMap() {
        buckets = (Node<K,V>[]) new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= buckets.length * LOAD_FACTOR) {
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

        Node<K,V> newNode = new Node<>(key, value);
        newNode.next = buckets[index];
        buckets[index] = newNode;
        size++;
    }

    @Override
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

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        int hash = key == null ? 0 : key.hashCode() % buckets.length;
        return hash < 0 ? hash * -1 : hash;
    }

    private void resize() {
        Node<K, V>[] oldBuckets = buckets;
        int oldCapacity = buckets.length;
        int capacity = oldCapacity * CAPACITY_MULTIPLIER;
        buckets = (Node<K, V>[]) new Node[capacity];
        size = 0;

        for (int i = 0; i < oldCapacity; i++) {
            Node<K, V> node = oldBuckets[i];

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

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
