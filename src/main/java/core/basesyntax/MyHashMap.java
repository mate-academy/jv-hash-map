package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int DOUBLE_LENGTH = 2;
    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= LOAD_FACTOR * buckets.length) {
            resize();
        }
        int bucketIndex = getBucketIndex(key);
        Node<K, V> existing = buckets[bucketIndex];
        while (existing != null) {
            if (Objects.equals(key, existing.key)) {
                existing.value = value;
                return;
            }
            existing = existing.next;
        }
        Node<K, V> newNode = new Node<>(key, value, buckets[bucketIndex]);
        buckets[bucketIndex] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getBucketIndex(key);
        Node<K, V> node = buckets[bucketIndex];
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
        Node<K, V>[] oldBuckets = buckets;
        buckets = new Node[oldBuckets.length * DOUBLE_LENGTH];
        size = 0;
        for (Node<K, V> headNode : oldBuckets) {
            while (headNode != null) {
                put(headNode.key, headNode.value);
                headNode = headNode.next;
            }
        }
    }

    private int getBucketIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % buckets.length;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
