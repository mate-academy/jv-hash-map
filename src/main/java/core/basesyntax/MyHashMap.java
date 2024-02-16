package core.basesyntax;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int GROW_FACTOR = 2;
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private List<Entry<K, V>>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new ArrayList[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if ((double) size / buckets.length > LOAD_FACTOR) {
            resize();
        }

        int index = getIndex(key);
        if (buckets[index] == null) {
            buckets[index] = new ArrayList<>();
        }
        for (Entry<K, V> entry : buckets[index]) {
            if (Objects.equals(entry.key, key)) {
                entry.value = value;
                return;
            }
        }
        buckets[index].add(new Entry<>(key, value));
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        if (buckets[index] != null) {
            for (Entry<K, V> entry : buckets[index]) {
                if (Objects.equals(entry.key, key)) {
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

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % buckets.length);
    }

    private void resize() {
        if ((double) size / buckets.length > LOAD_FACTOR) {
            List<Entry<K, V>>[] oldBuckets = buckets;
            buckets = new ArrayList[oldBuckets.length * GROW_FACTOR];
            size = 0;
            for (List<Entry<K, V>> bucket : oldBuckets) {
                if (bucket != null) {
                    for (Entry<K, V> entry : bucket) {
                        put(entry.key, entry.value);
                    }
                }
            }
        }
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
