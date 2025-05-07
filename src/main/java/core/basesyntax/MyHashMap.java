package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Entry<K, V>[] table;
    private int size;

    public MyHashMap() {
        this(DEFAULT_CAPACITY, LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity, float loadFactor) {
        table = new Entry[initialCapacity];
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index = calculateIndex(key, table.length);

        for (Entry<K, V> entry = table[index]; entry != null; entry = entry.next) {
            if ((entry.key == null && key == null)
                    || (entry.key != null && entry.key.equals(key))) {
                entry.value = value;
                return;
            }
        }
        addEntry(hash, key, value, index);
    }

    @Override
    public V getValue(K key) {
        int index = calculateIndex(key, table.length);

        for (Entry<K, V> entry = table[index]; entry != null; entry = entry.next) {
            if (Objects.equals(entry.key, key)) {
                return entry.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private int hash(Object key) {
        return key == null ? 0 : key.hashCode() ^ (key.hashCode() >>> 16);
    }

    private void addEntry(int hash, K key, V value, int index) {
        Entry<K, V> newEntry = new Entry<>(key, value, table[index]);
        table[index] = newEntry;
        if (++size >= calculateThreshold(table.length)) {
            resize(2 * table.length);
        }
    }

    private void resize(int newCapacity) {
        Entry<K, V>[] oldTable = table;
        table = new Entry[newCapacity];
        calculateThreshold(newCapacity);

        for (Entry<K, V> entry : oldTable) {
            while (entry != null) {
                Entry<K, V> next = entry.next;
                int index = indexFor(hash(entry.key), newCapacity);
                entry.next = table[index];
                table[index] = entry;
                entry = next;
            }
        }
    }

    private int calculateThreshold(int capacity) {
        return (int) (capacity * LOAD_FACTOR);
    }

    private int calculateIndex(K key, int tableLength) {
        int hash = hash(key);
        return indexFor(hash, tableLength);
    }

    private static class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

        public Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
