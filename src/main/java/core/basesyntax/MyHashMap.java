package core.basesyntax;

import java.util.ArrayList;
import java.util.List;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int GROW_FACTOR = 2;

    private List<Entry<K, V>>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new ArrayList[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        ensureCapacity();
        int index = getIndexFromKey(hash(key), buckets.length);
        List<Entry<K, V>> bucket = buckets[index];
        if (bucket == null) {
            bucket = new ArrayList<>();
            buckets[index] = bucket;
        }
        for (Entry<K, V> entry : bucket) {
            if (key == null && entry.key == null
                    || key != null && key.equals(entry.key)) {
                entry.value = value;
                return;
            }
        }
        bucket.add(new Entry<>(key, value));
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndexFromKey(hash(key), buckets.length);
        List<Entry<K, V>> bucket = buckets[index];
        if (bucket != null) {
            for (Entry<K, V> entry : bucket) {
                if (key == null && entry.key == null
                        || key != null && key.equals(entry.key)) {
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

    private int hash(K key) {
        return key == null ? 0 : key.hashCode() % buckets.length;
    }

    private void ensureCapacity() {
        if ((double) size / buckets.length > LOAD_FACTOR) {
            int newCapacity = buckets.length * GROW_FACTOR;
            List<Entry<K, V>>[] oldBuckets = buckets;
            buckets = new ArrayList[newCapacity];
            size = 0;
            for (List<Entry<K, V>> bucket : oldBuckets) {
                if (bucket != null) {
                    for (Entry<K, V> entry : bucket) {
                        int index = getIndexFromKey(entry.key.hashCode(), newCapacity);
                        if (buckets[index] == null) {
                            buckets[index] = new ArrayList<>();
                        }
                        buckets[index].add(entry);
                        size++;
                    }
                }
            }
        }
    }

    private static <K, V> int getIndexFromKey(int key, int newCapacity) {
        return Math.abs(key) % newCapacity;
    }

    private static class Entry<K, V> {
        private final K key;
        private V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
