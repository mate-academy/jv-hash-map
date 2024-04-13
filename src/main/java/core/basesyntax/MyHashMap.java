package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75f;
    private static final int RESIZE_FACTOR = 2;
    private Entry<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new Entry[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        if (buckets[index] == null) {
            buckets[index] = new Entry<>(key, value);
            size++;
        } else {
            Entry<K, V> entry = buckets[index];
            while (entry != null) {
                if (key == entry.key || key != null && key.equals(entry.key)) {
                    entry.value = value;
                    return;
                }
                entry = entry.next;
            }
            Entry<K, V> newEntry = new Entry<>(key, value);
            newEntry.next = buckets[index];
            buckets[index] = newEntry;
            size++;
        }
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
        return LOAD_FACTOR * buckets.length;
    }

    private void resize() {
        Entry<K, V>[] oldBuckets = buckets;
        buckets = new Entry[RESIZE_FACTOR * oldBuckets.length];
        size = 0;
        for (Entry<K, V> entry : oldBuckets) {
            while (entry != null) {
                put(entry.key, entry.value);
                entry = entry.next;
            }
        }
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % buckets.length;
    }

    private static class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
