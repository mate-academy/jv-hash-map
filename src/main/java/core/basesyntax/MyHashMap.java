package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_FACTOR = 2;

    private Entry<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Entry[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = (isNullKey(key)) ? 0 : getIndex(key);

        for (Entry<K, V> entry = table[index]; entry != null; entry = entry.next) {
            if ((isNullKey(key) && isNullKey(entry.key))
                    || (!isNullKey(key) && key.equals(entry.key))) {
                entry.value = value;
                return;
            }
        }

        addEntry(key, value, index);
    }

    private boolean isNullKey(K key) {
        return key == null;
    }

    private int getIndex(K key) {
        int hash = hash(key);
        return indexFor(hash, table.length);
    }

    @Override
    public V getValue(K key) {
        if (isNullKey(key)) {
            return getForNullKey();
        }

        int index = getIndex(key);

        for (Entry<K, V> entry = table[index]; entry != null; entry = entry.next) {
            if (key.equals(entry.key)) {
                return entry.value;
            }
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private V getForNullKey() {
        for (Entry<K, V> entry = table[0]; entry != null; entry = entry.next) {
            if (entry.key == null) {
                return entry.value;
            }
        }
        return null;
    }

    private int hash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private void addEntry(K key, V value, int index) {
        Entry<K, V> entry = table[index];
        table[index] = new Entry<>(key, value, entry);
        size++;

        if (size > table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    private void resize() {
        int newCapacity = table.length * RESIZE_FACTOR;
        Entry<K, V>[] newTable = new Entry[newCapacity];

        for (Entry<K, V> entry : table) {
            while (entry != null) {
                Entry<K, V> next = entry.next;
                int index = indexFor(hash(entry.key), newCapacity);
                entry.next = newTable[index];
                newTable[index] = entry;
                entry = next;
            }
        }
        table = newTable;
    }

    static class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

        Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Entry<?, ?> entry = (Entry<?, ?>) obj;
            return Objects.equals(key, entry.key)
                    && Objects.equals(value, entry.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }
}
