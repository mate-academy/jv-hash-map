package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] buckets;
    private int size;
    private int threshold;

    public MyHashMap() {
        this.buckets = new Node[DEFAULT_INITIAL_CAPACITY];
        this.threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        Node<K, V> currentNode = buckets[bucketIndex];

        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }

        Node<K, V> newNode = new Node<>(key, value, buckets[bucketIndex]);
        buckets[bucketIndex] = newNode;
        size++;

        if (size >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getBucketIndex(key);
        Node<K, V> currentNode = buckets[bucketIndex];

        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = buckets.length * 2;
        Node<K, V>[] newBuckets = new Node[newCapacity];

        for (Node<K, V> oldNode : buckets) {
            while (oldNode != null) {
                Node<K, V> nextNode = oldNode.next;
                int newIndex = getHash(oldNode.key) & (newCapacity - 1);
                oldNode.next = newBuckets[newIndex];
                newBuckets[newIndex] = oldNode;
                oldNode = nextNode;
            }
        }

        buckets = newBuckets;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
    }

    private int getBucketIndex(K key) {
        return getHash(key) & (buckets.length - 1);
    }

    private int getHash(K key) {
        int hash = (key == null) ? 0 : key.hashCode();
        return hash ^ (hash >>> 16);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
