package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOADFACTOR = 0.75;
    private static final int GROW_FACTOR = 2;
    private Entry<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new Entry[DEFAULT_INITIAL_CAPACITY];
    }

    public MyHashMap(int initialCapacity) {
        buckets = new Entry[initialCapacity];
    }

    public void put(K key, V value) {
        int threshold = (int) (buckets.length * DEFAULT_LOADFACTOR);
        if (size >= threshold) {
            resize();
        }
        int index = getIndexForEntry(key, buckets.length);
        Entry<K, V> e = buckets[index];
        while (e != null) {
            if (Objects.equals(e.key,key)) {
                e.value = value;
                return;
            }
            if (e.next == null) {
                break;
            }
            e = e.next;
        }
        if (e == null) {
            buckets[index] = new Entry<>(key, value);
        } else {
            e.next = new Entry<>(key, value);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndexForEntry(key, buckets.length);
        Entry<K, V> e = buckets[index];
        while (e != null) {
            if (Objects.equals(e.key, key)) {
                return e.value;
            }
            e = e.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = buckets.length * GROW_FACTOR;
        Entry<K, V>[] newBuckets = new Entry[newCapacity];
        for (Entry<K, V> entry : buckets) {
            while (entry != null) {
                Entry<K, V> next = entry.next;
                int index = (entry.key == null
                        ? 0 : getIndexForEntry(entry.key,newCapacity));
                entry.next = newBuckets[index];
                newBuckets[index] = entry;
                entry = next;
            }
        }
        buckets = newBuckets;
    }

    private int getIndexForEntry(K key, int capacity) {
        return Math.abs(Objects.hashCode(key) % capacity);
    }

    private static class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

        private Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
