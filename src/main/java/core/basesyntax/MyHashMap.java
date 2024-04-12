package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75f;
    private static final int RESIZE_FACTOR = 2;
    private Entry<K, V>[] buckets;
    private int size;
    private int capacity;
    private int resizeCount;

    public MyHashMap() {
        this(DEFAULT_CAPACITY);
    }

    public MyHashMap(int initialCapacity) {
        capacity = initialCapacity;
        buckets = new Entry[initialCapacity];
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Entry<K, V> entry = buckets[index];
        while (entry != null) {
            if (key == entry.key || key != null && key.equals(entry.key)) {
                entry.value = value;
                return;
            }
            entry = entry.next;
        }
        Entry<K,V> newEntry = new Entry<>(key, value);
        newEntry.next = buckets[index];
        buckets[index] = newEntry;
        size++;
        if (size > getThreshold()) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Entry<K, V> entry = buckets[index];

        while (entry != null) {
            if (key == entry.key || key != null && key.equals(entry.key)) {
                return entry.value;
            }
            entry = entry.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private double getThreshold() {
        return LOAD_FACTOR * (double) DEFAULT_CAPACITY * Math.pow(RESIZE_FACTOR, resizeCount);
    }

    private void resize() {
        int newCapacity = buckets.length * RESIZE_FACTOR;
        Entry<K, V>[] newBuckets = new Entry[newCapacity];

        for (int i = 0; i < buckets.length; i++) {
            Entry<K, V> entry = buckets[i];
            while (entry != null) {
                Entry<K, V> next = entry.next;
                entry.next = null;
                int index = getIndex(entry.key, newCapacity);
                putInNewBucket(newBuckets, entry.key, entry.value);
                entry = next;
            }
        }
        buckets = newBuckets;
        capacity = newCapacity;
        resizeCount++;
    }

    private void putInNewBucket(Entry<K, V>[] newBuckets, K key, V value) {
        int index = getIndex(key, newBuckets.length);

        if (newBuckets[index] == null) {
            newBuckets[index] = new Entry<>(key, value);
        } else {
            Entry<K, V> current = newBuckets[index];
            while (current.next != null) {
                current = current.next;
            }
            current.next = new Entry<>(key, value);
        }
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % buckets.length;
    }

    private int getIndex(K key, int capacity) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % capacity;
    }

    private static class Entry<K, V> {
        private K key;
        private V value;
        private Entry<K, V> next;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
