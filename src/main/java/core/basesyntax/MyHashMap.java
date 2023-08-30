package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Entry<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new Entry[INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Entry<K, V> entry = buckets[index];
        while (entry != null) {
            if ((key == null && entry.key == null)
                    || (key != null && key.equals(entry.key))) {
                entry.value = value;
                return;
            }
            entry = entry.next;
        }
        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.next = buckets[index];
        buckets[index] = newEntry;
        size++;
        resize();
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Entry<K, V> entry = buckets[index];
        while (entry != null) {
            if ((key == null && entry.key == null)
                    || (key != null && key.equals(entry.key))) {
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

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % buckets.length;
    }

    private void resize() {
        if ((double) size / buckets.length >= LOAD_FACTOR) {
            Entry<K, V>[] newBuckets = new Entry[buckets.length * 2];
            for (Entry<K, V> entry : buckets) {
                while (entry != null) {
                    Entry<K, V> temp = entry.next;
                    entry.next = null;
                    int newIndex = Math.abs(entry.key.hashCode()) % newBuckets.length;
                    entry.next = newBuckets[newIndex];
                    newBuckets[newIndex] = entry;
                    entry = temp;
                }
            }
            buckets = newBuckets;
        }
    }

    private static class Entry<K, V> {
        private K key;
        private V value;
        private Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
