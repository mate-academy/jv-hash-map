package core.basesyntax;

import java.util.LinkedList;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int DOUBLE_LENGTH = 2;
    private LinkedList<Entry<K, V>>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new LinkedList[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= LOAD_FACTOR * buckets.length) {
            resize();
        }
        int bucketIndex = getBucketIndex(key, buckets.length);
        if (buckets[bucketIndex] == null) {
            buckets[bucketIndex] = new LinkedList<>();
        }
        LinkedList<Entry<K, V>> bucket = buckets[bucketIndex];
        for (Entry<K, V> entry : bucket) {
            if (Objects.equals(key, entry.key)) {
                entry.value = value;
                return;
            }
        }
        bucket.add(new Entry<>(key, value));
        size++;
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getBucketIndex(key, buckets.length);
        LinkedList<Entry<K, V>> bucket = buckets[bucketIndex];
        if (bucket != null) {
            for (Entry<K, V> entry : bucket) {
                if (Objects.equals(key, entry.key)) {
                    return entry.value;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        LinkedList<Entry<K, V>>[] oldBuckets = buckets;
        buckets = new LinkedList[oldBuckets.length * DOUBLE_LENGTH];
        size = 0;
        for (LinkedList<Entry<K, V>> bucket : oldBuckets) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    put(entry.key, entry.value);
                }
            }
        }
    }

    private int getBucketIndex(K key, int bucketsLength) {
        return key == null ? 0 : Math.abs(key.hashCode()) % bucketsLength;
    }

    private static class Entry<K, V> {
        private final K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
