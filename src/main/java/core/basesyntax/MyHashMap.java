package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int SCALE_FACTOR = 2;
    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap(int capacity) {
        this.buckets = (Node<K, V>[]) new Node[capacity];
    }

    public MyHashMap() {
        this(INITIAL_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        int bucketIndex = getIndex(key);
        Node<K, V> currentNode = buckets[bucketIndex];
        while (currentNode != null) {
            if (isKeysEquals(key, currentNode)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = buckets[bucketIndex];
        buckets[bucketIndex] = newNode;
        size++;
        if ((double) size / buckets.length >= LOAD_FACTOR) {
            resizeMap();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = buckets[index];
        while (node != null) {
            if (isKeysEquals(key, node)) {
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

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % buckets.length);
    }

    private boolean isKeysEquals(K key, Node<K, V> node) {
        return Objects.equals(node.key, key);
    }

    private void resizeMap() {
        int newCapasity = buckets.length * SCALE_FACTOR;
        Node<K, V>[] newBuckets = new Node[newCapasity];
        for (Node<K, V> currentNode : buckets) {
            while (currentNode != null) {
                int index = Math.abs(currentNode.key.hashCode() % newCapasity);
                Node<K, V> next = currentNode.next;
                currentNode.next = newBuckets[index];
                newBuckets[index] = currentNode;
                currentNode = next;
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
