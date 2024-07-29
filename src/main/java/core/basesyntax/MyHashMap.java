package core.basesyntax;

import java.util.LinkedList;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int capacity;
    private int size = 0;
    private LinkedList<Entry<K, V>>[] buckets;
    private Entry<K, V> nullKeyEntry;

    public MyHashMap() {
        this.capacity = INITIAL_CAPACITY;
        buckets = new LinkedList[capacity];
    }

    private static class Entry<K, V> {
        private K key;
        private V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public void put(K key, V value) {
        if (key == null) {
            if (nullKeyEntry == null) {
                size++;
            }
            nullKeyEntry = new Entry<>(key, value);
            return;
        }

        int index = getBucketIndex(key);
        if (buckets[index] == null) {
            buckets[index] = new LinkedList<>();
        }

        for (Entry<K, V> entry : buckets[index]) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }

        buckets[index].add(new Entry<>(key, value));
        size++;

        if (size >= capacity * LOAD_FACTOR) {
            resize();
        }
    }

    public V getValue(K key) {
        if (key == null) {
            return nullKeyEntry != null ? nullKeyEntry.value : null;
        }

        int index = getBucketIndex(key);
        LinkedList<Entry<K, V>> bucket = buckets[index];

        if (bucket == null) {
            return null;
        }

        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }

        return null;
    }

    public int getSize() {
        return size;
    }

    private int getBucketIndex(K key) {
        return Math.abs(key.hashCode() % capacity);
    }

    private void resize() {
        int newCapacity = capacity * 2;
        LinkedList<Entry<K, V>>[] newBuckets = new LinkedList[newCapacity];

        for (LinkedList<Entry<K, V>> bucket : buckets) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    int newIndex = Math.abs(entry.key.hashCode() % newCapacity);
                    if (newBuckets[newIndex] == null) {
                        newBuckets[newIndex] = new LinkedList<>();
                    }
                    newBuckets[newIndex].add(entry);
                }
            }
        }

        buckets = newBuckets;
        capacity = newCapacity;
    }
}
