package core.basesyntax;

import java.util.ArrayList;
import java.util.List;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int MULTIPLIER = 2;
    private List<Node<K, V>>[] buckets = new ArrayList[INITIAL_CAPACITY];
    private int size;

    public int index(K key) {
        return key == null ? 0 : key.hashCode() % buckets.length;
    }

    @Override
    public void put(K key, V value) {
        int bucketIndex = index(key);
        List<Node<K, V>> bucket = buckets[bucketIndex];

        if (bucket == null) {
            bucket = new ArrayList<>();
            buckets[bucketIndex] = bucket;
        }

        for (int i = 0; i < bucket.size(); i++) {
            Node<K, V> entry = bucket.get(i);
            if (entry.getNodeKey().equals(key)) {
                entry.setNodeValue(value);
                return;
            }
        }

        bucket.add(new Node<>(key, value));
        size++;

        if ((double) size / buckets.length > LOAD_FACTOR) {
            increaseSize();
        }
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = index(key);
        List<Node<K, V>> bucket = buckets[bucketIndex];
        if (bucket != null) {
            for (Node<K, V> entry : bucket) {
                if (entry.getNodeKey().equals(key)) {
                    return entry.getNodeValue();
                }
            }
        }
        return null;
    }

    public void increaseSize() {
        int newCapacity = buckets.length * MULTIPLIER;
        List<Node<K, V>>[] newBiggerBucket = new ArrayList[newCapacity];
        for (List<Node<K, V>> bucket : buckets) {
            if (bucket != null) {
                for (Node<K, V> entry : bucket) {
                    int index = entry.getNodeKey() == null ? 0 : entry.getNodeKey().hashCode() % newCapacity;

                    if (newBiggerBucket[index] == null) {
                        newBiggerBucket[index] = new ArrayList<>();
                    }
                    newBiggerBucket[index].add(entry);
                }
            }
        }

        buckets = newBiggerBucket;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K, V> {
        private K key;
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getNodeKey() {
            return key;
        }

        public V getNodeValue() {
            return value;
        }

        public void setNodeValue(V value) {
            this.value = value;
        }
    }
}