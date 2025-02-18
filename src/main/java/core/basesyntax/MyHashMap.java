package core.basesyntax;

import java.util.LinkedList;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private LinkedList<Node<K, V>>[] buckets;
    private int size;
    private int threshold;
    private Node<K, V> nullNode;

    public MyHashMap() {
        this(DEFAULT_CAPACITY);
    }

    public MyHashMap(int capacity) {
        buckets = new LinkedList[capacity];
        size = 0;
        threshold = (int) (capacity * LOAD_FACTOR);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }

        if (key == null) {
            if (nullNode == null) {
                size++;
            }
            nullNode = new Node<>(null, value);
            return;
        }

        int index = Math.abs(key.hashCode() % buckets.length);
        if (buckets[index] == null) {
            buckets[index] = new LinkedList<>();
        }
        for (Node<K, V> node : buckets[index]) {
            if (node.key.equals(key)) {
                node.value = value;
                return;
            }
        }
        buckets[index].add(new Node<>(key, value));
        size++;
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return (nullNode != null) ? nullNode.value : null;
        }
        int index = Math.abs(key.hashCode() % buckets.length);
        if (buckets[index] == null) {
            return null;
        }
        for (Node<K, V> node : buckets[index]) {
            if (node.key.equals(key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = buckets.length * 2;
        LinkedList<Node<K, V>>[] newBuckets = new LinkedList[newCapacity];

        for (LinkedList<Node<K, V>> bucket : buckets) {
            if (bucket != null) {
                for (Node<K, V> node : bucket) {
                    int newIndex = Math.abs(node.key.hashCode() % newCapacity);
                    if (newBuckets[newIndex] == null) {
                        newBuckets[newIndex] = new LinkedList<>();
                    }
                    newBuckets[newIndex].add(node);
                }
            }
        }

        buckets = newBuckets;
        threshold = (int) (newCapacity * LOAD_FACTOR);
    }
}
