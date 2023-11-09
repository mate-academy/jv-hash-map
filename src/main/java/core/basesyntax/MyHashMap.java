package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Entry<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new Entry[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        int bucketIndex = getBucketIndex(key);

        Entry<K, V> entry = buckets[bucketIndex];
        while (entry != null) {
            if (key == null ? entry.key == null : key.equals(entry.key)) {
                entry.value = value;
                return;
            }
            entry = entry.next;
        }

        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.next = buckets[bucketIndex];
        buckets[bucketIndex] = newEntry;

        size++;

        if (needToResize()) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getBucketIndex(key);

        Entry<K, V> entry = buckets[bucketIndex];
        while (entry != null) {
            if (key == null ? entry.key == null : key.equals(entry.key)) {
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

    private int getBucketIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % buckets.length;
    }

    private void resize() {
        Entry<K, V>[] newBuckets = new Entry[buckets.length * 2];
        for (int i = 0; i < newBuckets.length; i++) {
            newBuckets[i] = null;
        }

        for (Entry<K, V> entry : buckets) {
            while (entry != null) {
                int newIndex = getIndex(entry.key, newBuckets.length);
                Entry<K, V> next = entry.next;

                entry.next = newBuckets[newIndex];
                newBuckets[newIndex] = entry;

                entry = next;
            }
        }

        buckets = newBuckets;
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

    private boolean needToResize() {
        return (double) size / buckets.length > LOAD_FACTOR;
    }

    private int getIndex(K key, int newBucketsLength) {
        return Math.abs(key.hashCode()) % newBucketsLength;
    }
}

