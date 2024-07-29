package core.basesyntax;

import java.util.LinkedList;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private int initialCapacity = 16;
    private float loadFactor = 0.75f;
    private int size = 0;
    private LinkedList<Entry<K, V>>[] buckets;
    private Entry<K, V> nullKeyEntry;

    public MyHashMap() {
        buckets = new LinkedList[initialCapacity];
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

        if (size >= initialCapacity * loadFactor) {
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
        return Math.abs(key.hashCode() % initialCapacity);
    }

    private void resize() {
        initialCapacity *= 2;
        LinkedList<Entry<K, V>>[] newBuckets = new LinkedList[initialCapacity];

        for (LinkedList<Entry<K, V>> bucket : buckets) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    int newIndex = Math.abs(entry.key.hashCode() % initialCapacity);
                    if (newBuckets[newIndex] == null) {
                        newBuckets[newIndex] = new LinkedList<>();
                    }
                    newBuckets[newIndex].add(entry);
                }
            }
        }

        buckets = newBuckets;
    }
}
