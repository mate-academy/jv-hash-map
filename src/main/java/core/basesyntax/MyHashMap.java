package core.basesyntax;

import java.util.ArrayList;
import java.util.List;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private List<Entry<K, V>>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new ArrayList[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putForNullKey(value);
            return;
        }
        int index = getIndex(key);
        if (buckets[index] == null) {
            buckets[index] = new ArrayList<>();
        }
        for (Entry<K, V> entry : buckets[index]) {
            if (entry.getKey() == null) {
                continue; // Skip entries with null keys
            }
            if (entry.getKey().equals(key)) {
                entry.setValue(value);
                return;
            }
        }
        buckets[index].add(new Entry<>(key, value));
        size++;
        if ((double) size / buckets.length > LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getValueForNullKey();
        }
        int index = getIndex(key);
        if (buckets[index] != null) {
            for (Entry<K, V> entry : buckets[index]) {
                if (entry.getKey() == null) {
                    continue; // Skip entries with null keys
                }
                if (key.equals(entry.getKey())) {
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

    private int getIndex(K key) {
        return Math.abs(key.hashCode() % buckets.length);
    }

    private void resize() {
        List<Entry<K, V>>[] oldBuckets = buckets;
        buckets = new ArrayList[oldBuckets.length * 2];
        size = 0;
        for (List<Entry<K, V>> bucket : oldBuckets) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    put(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    private void putForNullKey(V value) {
        List<Entry<K, V>> nullKeyBucket = buckets[0];
        if (nullKeyBucket != null) {
            for (Entry<K, V> entry : nullKeyBucket) {
                if (entry.getKey() == null) {
                    entry.setValue(value);
                    return;
                }
            }
        } else {
            buckets[0] = new ArrayList<>();
        }
        buckets[0].add(new Entry<>(null, value));
        size++;
    }

    private V getValueForNullKey() {
        List<Entry<K, V>> nullKeyBucket = buckets[0];
        if (nullKeyBucket != null) {
            for (Entry<K, V> entry : nullKeyBucket) {
                if (entry.getKey() == null) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    private static class Entry<K, V> {
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

