package core.basesyntax;

import java.util.ArrayList;
import java.util.List;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private List<Node<K, V>>[] buckets;
    private int size;

    private static class Node<K, V> {
        private final K key;
        private V value;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    MyHashMap() {
        this(DEFAULT_CAPACITY);
    }

    MyHashMap(int capacity) {
        buckets = new List[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new ArrayList<>();
        }
        size = 0;
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % buckets.length);
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        List<Node<K, V>> bucket = buckets[index];

        for (Node<K, V> node : bucket) {
            if ((node.key == null && key == null) || (node.key != null && node.key.equals(key))) {
                node.value = value;
                return;
            }
        }

        Node<K, V> newNode = new Node<>(key, value);
        bucket.add(newNode);
        size++;

        if ((double) size / buckets.length > LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        List<Node<K, V>> bucket = buckets[index];

        for (Node<K, V> node : bucket) {
            if ((node.key == null && key == null) || (node.key != null && node.key.equals(key))) {
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
        List<Node<K, V>>[] newBuckets = new List[newCapacity];
        for (int i = 0; i < newCapacity; i++) {
            newBuckets[i] = new ArrayList<>();
        }

        for (List<Node<K, V>> bucket : buckets) {
            for (Node<K, V> node : bucket) {
                int newIndex = Math.abs(node.key.hashCode() % newCapacity);
                newBuckets[newIndex].add(node);
            }
        }

        buckets = newBuckets;
    }
}
