package core.basesyntax;

import java.util.ArrayList;
import java.util.List;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private List<Entry<K, V>>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new List[DEFAULT_CAPACITY];
        for (int i = 0; i < DEFAULT_CAPACITY; i++) {
            buckets[i] = new ArrayList<>();
        }
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        List<Entry<K, V>> bucket = buckets[bucketIndex];

        if (key == null) {
            for (Entry<K, V> entry : bucket) {
                if (entry.getKey() == null) {
                    entry.setValue(value);
                    return;
                }
            }
            bucket.add(new Entry<>(null, value));
            size++;
        } else {
            for (Entry<K, V> entry : bucket) {
                if (key.equals(entry.getKey())) {
                    entry.setValue(value);
                    return;
                }
            }
            bucket.add(new Entry<>(key, value));
            size++;
        }

        if ((double) size / buckets.length > LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getBucketIndex(key);
        List<Entry<K, V>> bucket = buckets[bucketIndex];

        if (key == null) {
            for (Entry<K, V> entry : bucket) {
                if (entry.getKey() == null) {
                    return entry.getValue();
                }
            }
        } else {
            for (Entry<K, V> entry : bucket) {
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

    private int getBucketIndex(K key) {
        if (key == null) {
            return 0; // Я обрав 0, але ви можете вибрати інший індекс за вашими потребами
        }
        return Math.abs(key.hashCode()) % buckets.length;
    }

    private void resize() {
        List<Entry<K, V>>[] newBuckets = new List[buckets.length * 2];
        for (int i = 0; i < newBuckets.length; i++) {
            newBuckets[i] = new ArrayList<>();
        }

        for (List<Entry<K, V>> bucket : buckets) {
            for (Entry<K, V> entry : bucket) {
                int newIndex = Math.abs(entry.getKey().hashCode()) % newBuckets.length;
                newBuckets[newIndex].add(entry);
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
