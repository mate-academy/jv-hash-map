package core.basesyntax;

import java.util.ArrayList;
import java.util.List;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private int size;
    private int threshold;
    private List<Entry<K, V>>[] buckets;

    public MyHashMap() {
        this(INITIAL_CAPACITY);
    }

    public MyHashMap(int capacity) {
        this.buckets = new List[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new ArrayList<>();
        }
        this.threshold = (int) (capacity * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = getIndex(key);
        List<Entry<K, V>> bucket = buckets[index];

        // Обробка ключа null
        if (key == null) {
            for (Entry<K, V> entry : bucket) {
                if (entry.getKey() == null) {
                    entry.setValue(value);
                    return;
                }
            }
            bucket.add(new Entry<>(null, value));
            size++;
            return;
        }

        for (Entry<K, V> entry : bucket) {
            if (entry.getKey() != null && entry.getKey().equals(key)) {
                entry.setValue(value);
                return;
            }
        }
        bucket.add(new Entry<>(key, value));
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        List<Entry<K, V>> bucket = buckets[index];

        if (key == null) {
            for (Entry<K, V> entry : bucket) {
                if (entry.getKey() == null) {
                    return entry.getValue();
                }
            }
            return null;
        }

        for (Entry<K, V> entry : bucket) {
            if (entry.getKey() != null && entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        } else {
            return Math.abs(key.hashCode()) % buckets.length;
        }
    }

    private void resize() {
        int newCapacity = buckets.length * 2;
        List<Entry<K, V>>[] newBuckets = new List[newCapacity];

        for (int i = 0; i < newCapacity; i++) {
            newBuckets[i] = new ArrayList<>();
        }

        for (List<Entry<K, V>> bucket : buckets) {
            for (Entry<K, V> entry : bucket) {
                int newIndex = entry.getKey() == null ? 0 :
                        Math.abs(entry.getKey().hashCode()) % newCapacity;
                newBuckets[newIndex].add(entry);
            }
        }

        buckets = newBuckets;
        threshold = (int) (newCapacity * LOAD_FACTOR);
    }
}
