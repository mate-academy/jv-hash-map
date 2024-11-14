package core.basesyntax;

import java.util.LinkedList;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private LinkedList<Entry<K, V>>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new LinkedList[INITIAL_CAPACITY];
        size = 0;
    }

    private int getBucketIndex(K key) {
        return Math.abs(key.hashCode()) % buckets.length;
    }

    @Override
    public void put(K key, V value) {
        int index = getBucketIndex(key);
        if (size >= buckets.length * LOAD_FACTOR) {
            resize();
        }
        if (buckets[index] == null) {
            buckets[index] = new LinkedList<>();
        }
        for (Entry<K, V> entry: buckets[index]) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }
        buckets[index].add(new Entry<>(key, value));
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getBucketIndex(key);
        if (buckets[index] == null) {
            return null;
        }
        for (Entry<K, V> entry: buckets[index]) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = buckets.length * 2;
        LinkedList<Entry<K, V>>[] newBuckets = new LinkedList[newCapacity];
        for (LinkedList<Entry<K, V>> bucket: buckets) {
            if (bucket != null) {
                for (Entry<K, V> entry: bucket) {
                    int newIndex = Math.abs(entry.key.hashCode()) % newCapacity;
                    if (newBuckets[newIndex] == null) {
                        newBuckets[newIndex] = new LinkedList<>();
                    }
                    newBuckets[newIndex].add(entry);
                }
            }
        }
        buckets = newBuckets;
    }

    private static class Entry<K, V> {
        private K key;
        private V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
