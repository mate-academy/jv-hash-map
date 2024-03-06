package core.basesyntax;

import java.util.ArrayList;
import java.util.List;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    private final double defaultLoadFactor = 0.75;
    private int threshold = (int) (DEFAULT_INITIAL_CAPACITY * defaultLoadFactor);
    private Entry<K, V>[] buckets;
    private List<Entry<K, V>> nullBucket;
    private int size;

    private static class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

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

        void setValue(V value) {
            this.value = value;
        }
    }

    public MyHashMap() {
        buckets = new Entry[DEFAULT_INITIAL_CAPACITY];
        nullBucket = new ArrayList<>();
    }

    public MyHashMap(int initialCapacity) {
        this.threshold = (int) (initialCapacity * defaultLoadFactor);
        this.buckets = new Entry[initialCapacity];
        nullBucket = new ArrayList<>();
    }

    private void resize() {
        int newCapacity = buckets.length * 2;
        Entry<K, V>[] newBuckets = new Entry[newCapacity];
        for (Entry<K, V> entry : buckets) {
            while (entry != null) {
                Entry<K, V> next = entry.next;
                int index = (entry.getKey() == null
                        ? 0 : Math.abs(entry.getKey().hashCode() % newCapacity));
                entry.next = newBuckets[index];
                newBuckets[index] = entry;
                entry = next;
            }
        }
        buckets = newBuckets;
        threshold = (int) (newCapacity * defaultLoadFactor);
    }

    public void put(K key, V value) {
        if (key == null) {
            if (size + 1 > threshold) {
                resize();
            }
            for (Entry<K, V> entry : nullBucket) {
                if (entry.getKey() == null) {
                    entry.setValue(value);
                    return;
                }
            }
            nullBucket.add(new Entry<>(null, value));
            size++;
        } else {
            if (size + 1 > threshold) {
                resize();
            }
            int index = Math.abs(key.hashCode() % buckets.length);
            Entry<K, V> e = buckets[index];
            if (e == null) {
                buckets[index] = new Entry<>(key, value);
                size++;
            } else {
                while (e != null) {
                    if (e.getKey() == null || e.getKey().equals(key)) {
                        e.setValue(value);
                        return;
                    }
                    if (e.next == null) {
                        break;
                    }
                    e = e.next;
                }
                e.next = new Entry<>(key, value);
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            for (Entry<K, V> entry : nullBucket) {
                if (entry.getKey() == null) {
                    return entry.getValue();
                }
            }
            return null;
        }
        int index = Math.abs(key.hashCode() % buckets.length);
        Entry<K, V> e = buckets[index];
        while (e != null) {
            K currentKey = e.getKey();
            if (currentKey != null && currentKey.equals(key)) {
                return e.getValue();
            }
            e = e.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
