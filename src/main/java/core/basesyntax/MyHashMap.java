package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] buckets;

    public MyHashMap() {
        this.buckets = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (needsResize()) {
            resize();
        }
        int bucketIndex = getBucketIndex(key);
        Node<K, V> node = buckets[bucketIndex];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
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

    private int getBucketIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % buckets.length);
    }

    private boolean needsResize() {
        return (float) size / buckets.length > LOAD_FACTOR;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] temp = buckets;
        buckets = (Node<K, V>[]) new Node[buckets.length << 1];
        for (Node<K, V> kvNode : temp) {
            while (kvNode != null) {
                put(kvNode.key, kvNode.value);
                kvNode = kvNode.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
