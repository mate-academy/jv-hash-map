package core.basesyntax;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Map.Entry;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private LinkedList<Entry<K, V>>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new LinkedList[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        if (buckets[index] == null) {
            buckets[index] = new LinkedList<>();
        }

        for (Entry<K, V> entry : buckets[index]) {
            if ((entry.getKey() == null && key == null)
                    || (entry.getKey() != null && entry.getKey().equals(key))) {
                entry.setValue(value);
                return;
            }
        }

        buckets[index].add(new AbstractMap.SimpleEntry<>(key, value));
        size++;

        if ((double) size / buckets.length >= LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        if (buckets[index] == null) {
            return null;
        }

        for (Entry<K, V> entry : buckets[index]) {
            if ((entry.getKey() == null && key == null)
                    || (entry.getKey() != null && entry.getKey().equals(key))) {
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
        }
        return Math.abs(key.hashCode()) % buckets.length;
    }

    private void resize() {
        LinkedList<Entry<K, V>>[] newBuckets = new LinkedList[buckets.length * 2];
        for (LinkedList<Entry<K, V>> bucket : buckets) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    int index = getIndex(entry.getKey(), newBuckets.length);
                    if (newBuckets[index] == null) {
                        newBuckets[index] = new LinkedList<>();
                    }
                    newBuckets[index].add(entry);
                }
            }
        }
        buckets = newBuckets;
    }

    private int getIndex(K key, int length) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % length;
    }
}
