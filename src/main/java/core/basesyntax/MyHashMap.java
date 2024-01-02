package core.basesyntax;

import java.util.ArrayList;
import java.util.List;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private List<Entry<K, V>>[] buckets;
    private int size;

    public MyHashMap() {
        this.buckets = new ArrayList[DEFAULT_CAPACITY];
        this.size = 0;
    }

    @Override
    public void put(K key, V value) {
        ensureCapacity();
        int index = Math.abs(hash(key)) % buckets.length;
        List<Entry<K, V>> bucket = buckets[index];
        if (bucket == null) {
            bucket = new ArrayList<>();
            buckets[index] = bucket;
        }
        for (Entry<K, V> entry : bucket) {
            if ((key == null && entry.getKey() == null)
                    || (key != null && key.equals(entry.getKey()))) {
                entry.setValue(value);
                return;
            }
        }
        bucket.add(new Entry<>(key, value));
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = Math.abs(hash(key)) % buckets.length;
        List<Entry<K, V>> bucket = buckets[index];
        if (bucket != null) {
            for (Entry<K, V> entry : bucket) {
                if ((key == null && entry.getKey() == null)
                        || (key != null && key.equals(entry.getKey()))) {
                    return entry.getValue();
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
            int newCapacity = buckets.length * 2;
            List<Entry<K, V>>[] newBuckets = new ArrayList[newCapacity];
            for (List<Entry<K, V>> bucket : buckets) {
                if (bucket != null) {
                    for (Entry<K, V> entry : bucket) {
                        int index = Math.abs(entry.getKey().hashCode()) % newCapacity;
                        if (newBuckets[index] == null) {
                            newBuckets[index] = new ArrayList<>();
                        }
                        newBuckets[index].add(entry);
                    }
                }
            }
            buckets = newBuckets;
        }
    }

    class Entry<K, V> {
        private final K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }
}
