package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int GROWTH_FACTOR = 2;

    private Node<K, V>[] buckets;
    private int size;
    private int threshold;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        buckets = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = calculateThreshold(DEFAULT_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index = getIndex(hash, buckets.length);
        Node<K, V> currentNode = buckets[index];
        if (currentNode == null) {
            buckets[index] = new Node<>(hash, key, value, null);
        } else {
            while (true) {
                if (currentNode.hash == hash && Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = new Node<>(hash, key, value, null);
                    break;
                }
                currentNode = currentNode.next;
            }
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        Node<K, V> currentNode = buckets[getIndex(hash, buckets.length)];
        while (currentNode != null) {
            if (currentNode.hash == hash && Objects.equals(currentNode.key, key)) {
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

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] oldBuckets = buckets;
        int newCapacity = oldBuckets.length * GROWTH_FACTOR;
        buckets = (Node<K, V>[]) new Node[newCapacity];
        threshold = calculateThreshold(newCapacity);
        for (Node<K, V> headNode : oldBuckets) {
            Node<K, V> currentNode = headNode;
            while (currentNode != null) {
                Node<K, V> nextNode = currentNode.next;
                int index = getIndex(currentNode.hash, newCapacity);
                currentNode.next = buckets[index];
                buckets[index] = currentNode;
                currentNode = nextNode;
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int getIndex(int hash, int capacity) {
        return Math.abs(hash % capacity);
    }

    private int calculateThreshold(int capacity) {
        return (int) (capacity * LOAD_FACTOR);
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
