package core.basesyntax;

import java.util.LinkedList;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private LinkedList<Entry<K, V>>[] buckets;
    private int size;

    public MyHashMap() {
        this(DEFAULT_CAPACITY);
    }

    public MyHashMap(int initialCapacity) {
        buckets = new LinkedList[initialCapacity];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        int bucketIndex = (key == null) ? 0 : getBucketIndex(key);
        LinkedList<Entry<K, V>> bucket = buckets[bucketIndex];
        if (bucket == null) {
            bucket = new LinkedList<>();
            buckets[bucketIndex] = bucket;
        }
        for (Entry<K, V> entry : bucket) {
            if ((key == null && entry.key == null) || (key != null && key.equals(entry.key))) {
                entry.value = value;
                return;
            }
        }
        bucket.add(new Entry<>(key, value));
        size++;
        if ((double) size / buckets.length > LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = (key == null) ? 0 : getBucketIndex(key);
        LinkedList<Entry<K, V>> bucket = buckets[bucketIndex];
        if (bucket != null) {
            for (Entry<K, V> entry : bucket) {
                if ((key == null && entry.key == null) || (key != null && key.equals(entry.key))) {
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

    private int getBucketIndex(K key) {
        int hashCode = (key == null) ? 0 : key.hashCode();
        return Math.floorMod(hashCode, buckets.length);
    }

    private void resize() {
        int newCapacity = buckets.length * 2;
        LinkedList<Entry<K, V>>[] newBuckets = new LinkedList[newCapacity];
        for (LinkedList<Entry<K, V>> bucket : buckets) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    int newIndex = (entry.key == null)
                            ? 0 : Math.floorMod(entry.key.hashCode(), newCapacity);
                    LinkedList<Entry<K, V>> newBucket = newBuckets[newIndex];

                    if (newBucket == null) {
                        newBucket = new LinkedList<>();
                        newBuckets[newIndex] = newBucket;
                    }
                    newBucket.add(entry);
                }
            }
        }
        buckets = newBuckets;
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
